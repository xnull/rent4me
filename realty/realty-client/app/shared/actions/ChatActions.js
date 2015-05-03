/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var Constants = require('../constants/ChatConstants');
var Store = require('../stores/ChatStore');
var BlockUI = require('../common/BlockUI');
var WebSocketUtil = require('../common/WebSocketUtil');
var AuthStore = require('../stores/AuthStore');
var assign = require('object-assign');
var JSON = require('JSON2');

var Ajax = require('../common/Ajax');

var ChatActions = {

    subscribe: function() {
        var self = this;
        var wsAuthMessage = AuthStore.getWSAuthMessage();
        if(wsAuthMessage) {

            var _multiplexer = WebSocketUtil.getMultiplexer();
            var messagesChannel = _multiplexer.channel('messages');

            messagesChannel.onmessage = function(evt) {
                console.log('received data on messages channel');
                var msg = JSON.parse(evt.data);
                console.log(msg);
                AppDispatcher.handleViewAction({
                    actionType: Constants.CHAT_MESSAGE_ADDED,
                    message: msg
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