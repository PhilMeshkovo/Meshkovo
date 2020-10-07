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

Vue.component('message-form', {
    props: ['messages', 'messageAttr'],
    data: function () {
        return {
            phone: '',
            name: '',
            text: '',
            id: ''
        }
    },
    watch: {
        messageAttr: function (newVal, oldVal) {
            this.phone = newVal.phone;
            this.name = newVal.name;
            this.text = newVal.text;
            this.id = newVal.id;
        }
    },
    template:
        '<div id="save-form" style="position: absolute; right: 10px; text-align: center; width: 500px; display: none ">' +
        '<input type="text" style="text-align: center; background-color: #232323; color: white; width: 150px; height: 50px" placeholder="Write your text" v-model="text" />' +
        '<input type="text" style="text-align: center; background-color: #232323; color: white; width: 150px; height: 50px" placeholder="Write your name" v-model="name" />' +
        '<input type="text" style="text-align: center; background-color: #232323; color: white; width: 150px; height: 50px" placeholder="Write your phone" v-model="phone" />' +
        '<input type="button" onclick="window.location.reload()" style="text-align: center; background-color: #232323; color: white; width: 150px; float: outside" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function () {
            var message = {
                text: this.text,
                name: this.name,
                phone: this.phone
            };

            if (this.id) {
                messageApi.update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.messages, data.id);
                        this.messages.splice(index, 1, data);
                        this.phone = ''
                        this.name = ''
                        this.text = ''
                        this.id = ''
                    })
                )
            } else {
                messageApi.save({}, message).then(result =>
                    result.json().then(data => {
                        this.messages.push(data);
                        this.phone = ''
                        this.name = ''
                        this.text = ''
                    })
                )
            }
        }
    }
});

function showDiv() {
    var display = document.getElementById("save-form").style.display;
    if (display == 'none') {
        document.getElementById("save-form").style.display = 'block';
    } else {
        document.getElementById("save-form").style.display = 'none';
    }
}

