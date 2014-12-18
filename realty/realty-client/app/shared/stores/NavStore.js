/**
 * Created by dionis on 03/12/14.
 */
//var Dispatcher = require('flux/lib/Dispatcher');
var AppDispatcher = require('../dispatcher/AppDispatcher');

var EventEmitter = require('events').EventEmitter;
var NavConstants = require('../constants/NavConstants');

var assign = require('object-assign');

var _currentPage = 'home';

var CHANGE_EVENT = 'change';

//var UserStore = assign({}, EventEmitter.prototype, {
var NavStore = assign({}, EventEmitter.prototype, {
    emitChange: function() {
        this.emit(CHANGE_EVENT);
    },

    getCurrentPage: function() {
        return _currentPage;
    },

    isHomeSelected: function() {
        return _currentPage == 'home';
    },

    isLandLordSelected: function() {
        return _currentPage == 'landlord';
    },

    isRenterSelected: function(){
        return _currentPage == 'renter';
    },

    /**
     * @param {function} callback
     */
    addChangeListener: function(callback) {
        this.on(CHANGE_EVENT, callback);
    },
    /**
     * @param {function} callback
     */
    removeChangeListener: function(callback) {
        this.removeListener(CHANGE_EVENT, callback);
    }
});

console.log('Dispatcher registered: Nav store');
// Register to handle all updates
AppDispatcher.register(function(payload){
    var action = payload.action;
    console.log('Payload received in Nav dispatcher');
    console.log(payload);
    var page = action.page;

    switch(action.actionType) {
        case NavConstants.NAVIGATE_TO:
            console.log('Navigating to: '+page);
            _currentPage = page;
            break;
        default:
            return false;
    }


    // This often goes in each case that should trigger a UI change. This store
    // needs to trigger a UI change after every view action, so we can make the
    // code less repetitive by putting it here. We need the default case,
    // however, to make sure this only gets called after one of the cases above.
    NavStore.emitChange();
    return true; // No errors. Needed by promise in Dispatcher.
});

module.exports = NavStore;