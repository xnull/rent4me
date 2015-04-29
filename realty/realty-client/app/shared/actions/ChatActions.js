/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var Constants = require('../constants/ChatConstants');
var Store = require('../stores/ChatStore');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');
var JSON = require('JSON2');

var Ajax = require('../common/Ajax');
var SockJS = require('sockjs-client');
var socket = new SockJS('/ws/messages');
socket.onopen = function(){
    console.log('Connection open!');
    //setConnected(true);
};

socket.onclose = function(){
    console.log('Disconnecting connection');
};

socket.onmessage = function (evt)
{
    var received_msg = JSON.parse(evt.data);
    console.log(received_msg);
    console.log('message received!');
    AppDispatcher.handleViewAction({
        actionType: Constants.CHAT_MESSAGE_ADDED,
        message: received_msg
    });
    //showMessage(received_msg);
};

var ChatActions = {

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
                socket.send(JSON.stringify(data));

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