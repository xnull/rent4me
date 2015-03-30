/**
 * Created by dionis on 04/12/14.
 */
var AppDispatcher = require('../dispatcher/AppDispatcher');

var EventEmitter = require('rent4meEmitter');
var CityConstants = require('../constants/city-constants');

var assign = require('object-assign');

var _currentCity = null;

var CHANGE_EVENT = 'change';

var Store = assign({}, EventEmitter.prototype, {
    emitChange: function () {
        this.emit(CHANGE_EVENT);
    },

    /**
     *
     * @returns {{lat:*, lng: *}|null}
     */
    getCurrentCity: function () {
        return _currentCity || null;
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
        case CityConstants.CITY_CHANGED:
            console.log('city changed:');
            console.log(action.city);
            _currentCity = (action.city || null);
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