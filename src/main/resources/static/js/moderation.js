function getIndex(list, id) {
    for (let i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}


var messageApi = Vue.resource('/moderation{/id}');

Vue.component('message-form', {
    props: ['messages', 'messageAttr'],
    data: function () {
        return {
            status: '',
            id: ''
        }
    },
    watch: {
        messageAttr: function (newVal, oldVal) {
            this.status = newVal.status;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
        '<input type="text" placeholder="Write something" v-model="status" />' +
        '<input type="button" onclick="window.location.reload()" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function () {
            var message = {status: this.status};

            if (this.id) {
                messageApi.update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.messages, data.id);
                        this.messages.splice(index, 1, data);
                        this.status = ''
                        this.id = ''
                    })
                )
            } else {
                messageApi.save({}, message).then(result =>
                    result.json().then(data => {
                        this.messages.push(data);
                        this.status = ''
                    })
                )
            }
        }
    }
});

Vue.component('message-row', {
    props: ['message', 'editMethod', 'messages'],
    template: '<li style="list-style-position: inside; width: 50%">' +
        '{{ message.text }}. <strong>{{ message.name }}. {{ message.phone }}. {{ message.status}}</strong>' +
        '<span style="position: absolute; right: 5%">' +
        '<input type="button" value="Edit" @click="edit" />' +
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
    el: '#mod',
    template: '<messages-list :messages="messages" />',
    data: {
        messages: []
    }
});


