/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var Constants = require('../constants/ChatConstants');
var Store = require('../stores/ChatStore');
var BlockUI = require('../common/BlockUI');
var AuthStore = require('../stores/AuthStore');
var assign = require('object-assign');
var JSON = require('JSON2');

var Ajax = require('../common/Ajax');
var SockJS = require('sockjs-client');
var WebSocketMultiplexer = require('../common/WebSocketMultiplexClient');
console.log("Multiplexer");
console.log(WebSocketMultiplexer);

var _socket = null;
var _multiplexer = null;
var _recoveryCheckInterval = null;

var ChatActions = {

    subscribe: function() {
        var self = this;
        var wsAuthMessage = AuthStore.getWSAuthMessage();
        if(wsAuthMessage) {
            _socket = new SockJS('/ws');
            _multiplexer = new WebSocketMultiplexer(_socket);
            var messagesChannel = _multiplexer.channel('messages');
            _socket.onopen = function(){
                console.log('Connection open!');
                if(_recoveryCheckInterval) {
                    console.log('Clearing interval for recovery');
                    clearInterval(_recoveryCheckInterval);
                    _recoveryCheckInterval = null;
                }
                _socket.send(wsAuthMessage);
                //setConnected(true);
            };

            _socket.onclose = function(){
                console.log('Disconnecting connection');
                _socket = null;
                if(!_recoveryCheckInterval) {
                    console.log('Scheduling interval for recovery');
                    _recoveryCheckInterval = setInterval(function(){
                        self.subscribe();//try to reconnect
                    }, 5000);
                }
            };

            _socket.onerror = function(err){
                console.log('Error occurred');
                if(_socket) {
                    _socket.close();
                }
            };

            _socket.onmessage = function (evt)
            {
                console.log('message received on socket!');
                console.log(evt.data);
                try {
                    var received_msg = JSON.parse(evt.data);
                } catch (e) {
                    console.error("Non-parseable message");
                }
                /*if(!received_msg.status) {
                    AppDispatcher.handleViewAction({
                        actionType: Constants.CHAT_MESSAGE_ADDED,
                        message: received_msg
                    });
                }*/
                //showMessage(received_msg);
            };

            messagesChannel.onmessage = function(evt) {
                console.log('received data on messages channel');
                console.log(evt.data);
                AppDispatcher.handleViewAction({
                    actionType: Constants.CHAT_MESSAGE_ADDED,
                    message: JSON.parse(evt.data)
                });
            }
        }
    },

    unSubscribe: function() {
       /*if(_socket) {
           _socket.close();
           _socket = null;
       }*/
    },

    loadMyChats: function () {
        BlockUI.blockUI();

        //var limit = Store.getLimit();
        //var offset = Store.getOffset();

        Ajax
            .GET('/rest/users/me/chats')
            .authorized()
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: Constants.CHAT_LIST_LOADED,
                    chats: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    },

    sendNewMessage: function (personId, text, chatKey) {
        BlockUI.blockUI();

        var obj = {
            message: text,
            chat_key: chatKey
        };

        Ajax
            .POST('/rest/users/' + personId + '/chats')
            .authorized()
            .withJsonBody(obj)
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: Constants.CHAT_NEW_CONVERSATION_STARTED,
                    newMessage: data
                });
                AppDispatcher.handleViewAction({
                    actionType: Constants.CHAT_MESSAGE_ADDED,
                    message: data
                });
                //_socket.send(JSON.stringify(data));

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    },

    loadChatMessages: function (chatKey) {
        BlockUI.blockUI();

        Ajax
            .GET('/rest/users/me/chats/' + chatKey)
            .authorized()
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: Constants.CHAT_MESSAGES_LOADED,
                    messages: data,
                    chatKey: chatKey
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    }
};

module.exports = ChatActions;