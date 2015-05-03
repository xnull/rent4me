/**
 * Created by dionis on 04/12/14.
 */
var AppDispatcher = require('../dispatcher/AppDispatcher');

var EventEmitter = require('rent4meEmitter');
var Constants = require('../constants/notification-constants');

var assign = require('object-assign');
var JSON = require('JSON2');

var _notificationCache = {};

var CHANGE_EVENT = 'change';

var _autoResolveForChat = null;

var Store = assign({}, EventEmitter.prototype, {
    emitChange: function () {
        this.emit(CHANGE_EVENT);
    },

    addNotification: function(notification) {
        _notificationCache[notification.id] = notification;
    },

    addNotifications: function(notificationList) {
        var self = this;
        (notificationList || []).forEach(notification => {
            self.addNotification(notification);
        });
    },

    resolveNotifications: function(notificationIdList) {
        var self = this;
        (notificationIdList || []).forEach(notificationId => {
            var notification = _notificationCache[notificationId];
            if(notification) {
                notification.resolved = true;
            }
        });
    },

    getNotifications: function (type) {
        console.log("Notifications map:");
        console.log(_notificationCache);
        var result = [];

        for (var key in _notificationCache) {
            if (_notificationCache.hasOwnProperty(key)) {
                var obj = _notificationCache[key];
                if(!type || obj.type === type) {
                    result.push(obj);
                }
            }
        }

        return result;
    },

    getAutoResolveForChat: function() {
        return _autoResolveForChat;
    },

    listUnreadNotifications: function (type) {
        return this.getNotifications(type).filter(notification => !notification.resolved);
    },

    listUnreadNotificationsForChat: function (chatKey) {

        var listUnreadNotifications = this.listUnreadNotifications(Constants.NOTIFICATION_TYPE_NEW_MESSAGE);
        //alert('unread notifications: '+listUnreadNotifications.length);
        var filtered = listUnreadNotifications
            .filter(notification => {
                //alert(notification.chat_key);
                return notification.value.chat_key==chatKey;
            });
        //alert('unread notifications filtered: '+filtered.length);
        return filtered;
    },

    countOfUnreadNotifications: function(type) {
        return this.listUnreadNotifications(type).length;
    },

    /**
     * @param {function} callback
     */
    addChangeListener: function (callback) {
        this.on(CHANGE_EVENT, callback);
    },
    /**
     * @param {function} callback
     */
    removeChangeListener: function (callback) {
        this.removeListener(CHANGE_EVENT, callback);
    }
});

// Register to handle all updates
Store.dispatch = AppDispatcher.register(function (payload) {
    var action = payload.action;

    switch (action.actionType) {
        case Constants.NEW_NOTIFICATION_RECEIVED:
            console.log('new notification received:');
            console.log(action.notification);
            Store.addNotification(action.notification);
            break;
        case Constants.NOTIFICATIONS_LOADED:
            console.log('notifications loaded:');
            console.log(action.notifications);
            Store.addNotifications(action.notifications);
            break;
        case Constants.NOTIFICATIONS_RESOLVED:
            console.log('notifications resolved:');
            console.log(action.resolvedNotificationIds);
            Store.resolveNotifications(action.resolvedNotificationIds);
            break;
        case Constants.NOTIFICATIONS_AUTORESOLVE_FOR_CHAT:
            console.log('notifications auto resolve for chat:');
            console.log(action.chatKey);
            _autoResolveForChat = action.chatKey;
            return true;
            break;

        default:
            return false;
    }


    // This often goes in each case that should trigger a UI change. This store
    // needs to trigger a UI change after every view action, so we can make the
    // code less repetitive by putting it here. We need the default case,
    // however, to make sure this only gets called after one of the cases above.
    Store.emitChange();
    return true; // No errors. Needed by promise in Dispatcher.
});

module.exports = Store;