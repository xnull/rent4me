/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var Constants = require('../constants/notification-constants');
var Store = require('../stores/ChatStore');
var BlockUI = require('../common/BlockUI');
var WebSocketUtil = require('../common/WebSocketUtil');
var AuthStore = require('../stores/AuthStore');
var NotificationStore = require('../stores/notification-store');
var assign = require('object-assign');
var JSON = require('JSON2');

var Ajax = require('../common/Ajax');

var NotificationActions = {

    subscribe: function() {
        var self = this;
        var wsAuthMessage = AuthStore.getWSAuthMessage();
        if(wsAuthMessage) {

            var _multiplexer = WebSocketUtil.getMultiplexer();
            var messagesChannel = _multiplexer.channel('notifications');

            messagesChannel.onmessage = function(evt) {
                console.log('received data on messages channel');
                console.log(evt.data);
                AppDispatcher.handleViewAction({
                    actionType: Constants.NEW_NOTIFICATION_RECEIVED,
                    notification: JSON.parse(evt.data)
                });
                self.autoResolveNotifications();
            }
        }
    },

    unSubscribe: function() {
       /*if(_socket) {
           _socket.close();
           _socket = null;
       }*/
    },

    loadAllNotifications: function () {
        BlockUI.blockUI();
        var self = this;
        //var limit = Store.getLimit();
        //var offset = Store.getOffset();
        var promise = new Promise(function(resolve, reject){
            Ajax
                .GET('/rest/users/me/notifications/unread')
                .authorized()
                .onSuccess(function (data) {
                    BlockUI.unblockUI();
                    resolve(data);
                })
                .onError(function (xhr, status, err) {
                    BlockUI.unblockUI();
                    reject("Unable to load notifications");
                })
                .execute();
        }).then(function(data){
                AppDispatcher.handleViewAction({
                    actionType: Constants.NOTIFICATIONS_LOADED,
                    notifications: data
                });
                self.autoResolveNotifications();
            }, function(err){
                console.error(err);
            });
        return promise;
    },

    autoResolveNotifications: function() {
        var autoResolveForChat = NotificationStore.getAutoResolveForChat();
        if(autoResolveForChat) {
            console.log('auto-resolving notifications for chat '+autoResolveForChat);
            var notificationsToResolve = NotificationStore.listUnreadNotificationsForChat(autoResolveForChat);
            console.log('auto-resolving notifications: ');
            console.log(notificationsToResolve);
            this.resolveNotifications(notificationsToResolve);
        } else {
            console.log('NOT auto-resolving notifications ');
        }
    },

    autoResolveNotificationsForChat: function(chatKey) {
        AppDispatcher.handleViewAction({
            actionType: Constants.NOTIFICATIONS_AUTORESOLVE_FOR_CHAT,
            chatKey: chatKey
        });
    },

    resolveNotifications: function(notifications) {
        var notificationIds = (notifications || []).map(notification => notification.id);

        var promise = new Promise(function(resolve, reject){
            Ajax
                .PUT('/rest/users/me/notifications/resolve')
                .authorized()
                .withJsonBody({'notification_ids': notificationIds})
                .onSuccess(function (data) {
                    BlockUI.unblockUI();
                    resolve(data);
                })
                .onError(function (xhr, status, err) {
                    BlockUI.unblockUI();
                    reject("Unable to load notifications");
                })
                .execute();
        }).then(function(data){
                AppDispatcher.handleViewAction({
                    actionType: Constants.NOTIFICATIONS_RESOLVED,
                    resolvedNotificationIds: notificationIds
                });
            }, function(err){
                console.error(err);
            });
        return promise;
    }
};

module.exports = NotificationActions;