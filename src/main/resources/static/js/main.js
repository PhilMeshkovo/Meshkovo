function getIndex(list, id) {
    for (var i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}


var messageApi = Vue.resource('/message{/id}');

Vue.component('message-row', {
    props: ['message', 'editMethod', 'messages'],
    template: '<li style="list-style-position: inside">' +
        ' {{ message.text }}. <strong>{{ message.name }}. {{ message.phone }}</strong>' +
        '<span style="position: absolute; right: 0px">' +
        '</span>' +
        '</li>',
    methods: {
        edit: function () {
            this.editMethod(this.message);
        },
        del: function () {
            messageApi.remove({id: this.message.id}).then(result => {
                if (result.ok) {
                    this.messages.splice(this.messages.indexOf(this.message), 1)
                }
            })
        }
    }
});

Vue.component('messages-list', {
    props: ['messages'],
    data: function () {
        return {
            message: null
        }
    },
    template:
        '<div style="width: 500px">' +
        '<message-form :messages="messages" :messageAttr="message" />' +
        '<message-row v-for="message in messages" :key="message.id" :message="message" ' +
        ':editMethod="editMethod" :messages="messages" />' +
        '</div>',
    created: function () {
        messageApi.get().then(result =>
            result.json().then(data =>
                data.forEach(message => this.messages.push(message))
            )
        )
    },
    methods: {
        editMethod: function (message) {
            this.message = message;
        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<messages-list :messages="messages" />',
    data: {
        messages: []
    }
});

function showDiv() {
    var display = document.getElementById("save-div").style.display;
    if (display == 'none') {
        document.getElementById("save-div").style.display = 'block';
    } else {
        document.getElementById("save-div").style.display = 'none';
    }
}

