/**
 * Created by dionis on 04/12/14.
 */
var AppDispatcher = require('../dispatcher/AppDispatcher');

var EventEmitter = require('rent4meEmitter');
var SocialNetConstants = require('../constants/SocialNetConstants');

var assign = require('object-assign');

function emptyPost() {
    return {
        'location': null,
        'address': null,
        'type_of_rent': '',
        'rental_fee': null,
        'fee_period': '',
        'room_count': null,
        'floor_number': null,
        'floors_total': null,
        'area': null,
        'description': '',
        'photos': [],
        'added_photos_guids': [],
        'deleted_photos_guids': [],
        'published': false
    };
}

var _searchText = null;
var _searchWithSubway = false;
var _posts = [];
var _offset = 0;
var _limit = 30;
var _hasMoreResults = false;


var CHANGE_EVENT = 'change';

var Store = assign({}, EventEmitter.prototype, {
    emitChange: function() {
        this.emit(CHANGE_EVENT);
    },

    getSearchText: function() {
        return _searchText;
    },

    isSearchWithSubway: function() {
        return _searchWithSubway;
    },

    saveSearchResults: function(posts) {
        console.log('Saving search results:');
        var len = posts.length;
        console.log('Len: '+len);
        _hasMoreResults = len == _limit;
        console.log('Has more results?: '+_hasMoreResults);
        _offset += len;
        console.log('New offset: '+_offset);
        _posts = _posts.concat(posts);
        console.log('Posts in cache: ');
        console.log(_posts);
    },

    getSearchResults: function() {
        return _posts;
    },

    getOffset: function() {
        return _offset;
    },

    getLimit: function() {
        return _limit;
    },

    hasMoreSearchResults: function() {
        return _hasMoreResults;
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

// Register to handle all updates
AppDispatcher.register(function(payload){
    var action = payload.action;
//    console.log('Apartment store payload received in dispatcher');
    var apartmentObject = assign({}, action.apartment || {});
//    console.log("Action:");
//    console.log(action);
//    console.log("Apartment:");
//    console.log(apartmentObject);

    switch(action.actionType) {
        case SocialNetConstants.SOCIAL_NET_RENTER_POSTS_FOUND:
            console.log('found posts:');
            console.log(action.posts);
            Store.saveSearchResults(action.posts || []);
            break;

        case SocialNetConstants.SOCIAL_NET_RENTER_POSTS_RESET_SEARCH:
            _searchWithSubway = false;
            _searchText = null;
            _posts = [];
            _hasMoreResults = false;
            _offset = 0;
            break;

        case SocialNetConstants.SOCIAL_NET_RENTER_POSTS_SAVE_SEARCH_TEXT:
            _searchText = action.text || null;
            return true;//don't emit any event
            break;

        case SocialNetConstants.SOCIAL_NET_RENTER_POSTS_SAVE_SEARCH_WITH_SUBWAY:
            _searchWithSubway = action.value || false;
            return true;//don't emit any event
            break;

        default:
//            console.log("case: default");
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