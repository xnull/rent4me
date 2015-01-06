
/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var Constants = require('../constants/ChatConstants');
var Store = require('../stores/ChatStore');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');

var Ajax = require('../common/Ajax');

module.exports = {

    loadMyChats: function() {
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

    sendNewMessage: function(personId, text) {
        BlockUI.blockUI();

        //var limit = Store.getLimit();
        //var offset = Store.getOffset();

        var obj = {
            message: text
        };

        Ajax
            .POST('/rest/users/'+personId+'/chats')
            .authorized()
            .withJsonBody(obj)
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: Constants.CHAT_NEW_CONVERSATION_STARTED,
                    newMessage: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    },

    loadChatMessages: function(chatKey) {
        BlockUI.blockUI();

        //var limit = Store.getLimit();
        //var offset = Store.getOffset();

        Ajax
            .GET('/rest/users/me/chats/'+chatKey)
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
    },
};
