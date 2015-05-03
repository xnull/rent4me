/**
 * Created by dionis on 04/12/14.
 */
var AppDispatcher = require('../dispatcher/AppDispatcher');

var EventEmitter = require('rent4meEmitter');
var Constants = require('../constants/notification-constants');

var assign = require('object-assign');

var _notificationCache = {};

var CHANGE_EVENT = 'change';

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

    getNotifications: function () {
        console.log("Notifications map:");
        console.log(_notificationCache);
        var result = [];

        for (var key in _notificationCache) {
            if (_notificationCache.hasOwnProperty(key)) {
                var obj = _notificationCache[key];
                result.push(obj);
            }
        }

        return result;
    },

    listUnreadNotifications: function () {
        return this.getNotifications().filter(notification => !notification.resolved);
    },

    countOfUnreadNotifications: function() {
        return this.listUnreadNotifications().length;
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