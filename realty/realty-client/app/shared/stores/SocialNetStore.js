/**
 * Created by dionis on 04/12/14.
 */
var AppDispatcher = require('../dispatcher/AppDispatcher');

var EventEmitter = require('rent4meEmitter');
var SocialNetConstants = require('../constants/SocialNetConstants');

var Cookies = require('rent4meCookies');
var JSON2 = require('JSON2');

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
var _searchType = 'RENTER'; //'LESSOR' or 'RENTER'
var _searchRooms = {
    '1': false,
    '2': false,
    '3': false
};
var _searchMinPrice = null;
var _searchMaxPrice = null;
var _searchWithSubway = true;
var _posts = [];
var _offset = 0;
var _limit = 30;
var _hasMoreResults = false;
var _location = null;
var _countryCode = null;
var _bounds = null;
var _formattedAddress = null;
var _metros = [];

var CHANGE_EVENT = 'change';

var Store = assign({}, EventEmitter.prototype, {
    emitChange: function () {
        this.emit(CHANGE_EVENT);
    },

    getSearchText: function () {
        return _searchText;
    },

    isSearchWithSubway: function () {
        return _searchWithSubway;
    },

    getSearchType: function () {
        return _searchType;
    },

    getSearchRooms: function () {
        return _searchRooms;
    },

    getSearchMinPrice: function () {
        return _searchMinPrice;
    },

    getSearchMaxPrice: function () {
        return _searchMaxPrice;
    },

    getLocation: function(){
        return _location;
    },

    getCountryCode: function(){
        return _countryCode;
    },

    getBounds: function(){
        return _bounds;
    },

    getFormattedAddress: function(){
        return _formattedAddress;
    },

    getMetros: function() {
        return _metros || [];
    },

    saveSearchResults: function (posts) {
        console.log('Saving search results:');
        var len = posts.length;
        console.log('Len: ' + len);
        _hasMoreResults = len == _limit;
        console.log('Has more results?: ' + _hasMoreResults);
        _offset += len;
        console.log('New offset: ' + _offset);
        _posts = _posts.concat(posts);
        console.log('Posts in cache: ');
        console.log(_posts);
    },

    getSearchResults: function () {
        return _posts;
    },

    getOffset: function () {
        return _offset;
    },

    getLimit: function () {
        return _limit;
    },

    hasMoreSearchResults: function () {
        return _hasMoreResults;
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
AppDispatcher.register(function (payload) {
    var action = payload.action;
//    console.log('Apartment store payload received in dispatcher');
    var apartmentObject = assign({}, action.apartment || {});
//    console.log("Action:");
//    console.log(action);
//    console.log("Apartment:");
//    console.log(apartmentObject);

    switch (action.actionType) {
        case SocialNetConstants.SOCIAL_NET_POSTS_FOUND:
            console.log('found posts:');
            console.log(action.posts);
            Store.saveSearchResults(action.posts || []);
            break;

        case SocialNetConstants.SOCIAL_NET_POSTS_RESET_SEARCH:

            Cookies.deleteCookie('SEARCH_TYPE');

            Cookies.deleteCookie('SEARCH_ROOM_1');
            Cookies.deleteCookie('SEARCH_ROOM_2');
            Cookies.deleteCookie('SEARCH_ROOM_3');

            Cookies.deleteCookie('SEARCH_MIN_PRICE');
            Cookies.deleteCookie('SEARCH_MAX_PRICE');

            Cookies.deleteCookie('SEARCH_TEXT');
            //metros
            Cookies.deleteCookie('SEARCH_METROS');

            //google info
            Cookies.deleteCookie('SEARCH_BOUNDS');
            Cookies.deleteCookie('SEARCH_COUNTRY_CODE');
            Cookies.deleteCookie('SEARCH_LOCATION');
            Cookies.deleteCookie('SEARCH_FORMATTED_ADDRESS');

            _searchWithSubway = false;
            _searchText = null;
            _bounds = null;
            _countryCode = null;
            _location = null;
            _formattedAddress = null;
            _metros = [];
            _posts = [];
            _hasMoreResults = false;
            _offset = 0;
            break;

        case SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_TEXT:
            _searchText = action.text || null;

            Cookies.setCookieTemp('SEARCH_TEXT', encodeURIComponent(_searchText));

            return true;//don't emit any event
            break;

        case SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_WITH_SUBWAY:
            _searchWithSubway = action.value || false;
            return true;//don't emit any event
            break;

        case SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_TYPE:
            _searchType = action.value || 'RENTER';

            Cookies.setCookieTemp('SEARCH_TYPE', _searchType);

            return true;//don't emit any event
            break;

        case SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_ROOMS:
            _searchRooms = action.value;

            Cookies.setCookieTemp('SEARCH_ROOM_1', _searchRooms['1']);
            Cookies.setCookieTemp('SEARCH_ROOM_2', _searchRooms['2']);
            Cookies.setCookieTemp('SEARCH_ROOM_3', _searchRooms['3']);

            return true;//don't emit any event
            break;

        case SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_PRICE:
            var val = action.value || {};
            _searchMinPrice = val.min || null;
            _searchMaxPrice = val.max || null;

            Cookies.setCookieTemp('SEARCH_MIN_PRICE', _searchMinPrice);
            Cookies.setCookieTemp('SEARCH_MAX_PRICE', _searchMaxPrice);

            return true;//don't emit any event
            break;

        case SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_LOCATION_INFO:
        {
            var _val = action.value || {};
            _bounds = _val.bounds || null;
            _location = _val.location || null;
            _countryCode = _val.countryCode || null;
            _formattedAddress = _val.formattedAddress || null;

            if(_bounds ) {
                Cookies.setCookieTemp('SEARCH_BOUNDS', encodeURIComponent(JSON2.stringify(_bounds)));
            }
            if(_location ) {
                Cookies.setCookieTemp('SEARCH_LOCATION', encodeURIComponent(JSON2.stringify(_location)));
            }
            if(_countryCode ) {
                Cookies.setCookieTemp('SEARCH_COUNTRY_CODE', encodeURIComponent(JSON2.stringify(_countryCode)));
            }
            if(_formattedAddress ) {
                Cookies.setCookieTemp('SEARCH_FORMATTED_ADDRESS', encodeURIComponent(JSON2.stringify(_formattedAddress)));
            }

            return true;//don't emit any event
        }
            break;

        case SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_METROS:
        {
            var _selectedMetros = action.metros || [];

            console.log('selected metros');

            var _serialized_metros = encodeURIComponent(JSON2.stringify(_selectedMetros));

            Cookies.setCookieTemp('SEARCH_METROS', _serialized_metros);

            _metros = _selectedMetros;
            return true;//don't emit any event
        }
            break;


        case SocialNetConstants.SOCIAL_NET_POSTS_RESTORE_FROM_COOKIES_AND_CLEAR: {
            _searchType = Cookies.getCookie('SEARCH_TYPE') || _searchType;

            _searchRooms = {};
            _searchRooms['1'] = (Cookies.getCookie('SEARCH_ROOM_1') == 'true') || false;
            _searchRooms['2'] = (Cookies.getCookie('SEARCH_ROOM_2') == 'true') || false;
            _searchRooms['3'] = (Cookies.getCookie('SEARCH_ROOM_3') == 'true') || false;

            var minPriceCookie = Cookies.getCookie('SEARCH_MIN_PRICE');
            var maxPriceCookie = Cookies.getCookie('SEARCH_MAX_PRICE');

            _searchMinPrice = (!minPriceCookie || 'null' == minPriceCookie) ? null : parseInt(minPriceCookie);
            _searchMaxPrice = (!maxPriceCookie || 'null' == maxPriceCookie) ? null : parseInt(maxPriceCookie);

            var textCookie = Cookies.getCookie('SEARCH_TEXT');
            _searchText = (!textCookie || 'null' == textCookie) ? null : decodeURIComponent(textCookie);

            var metrosCookie = Cookies.getCookie('SEARCH_METROS');
            _metros = (!metrosCookie || 'null' == metrosCookie) ? [] : JSON2.parse(decodeURIComponent(metrosCookie));

            var boundsCookie = Cookies.getCookie('SEARCH_BOUNDS');
            _bounds = (!boundsCookie || 'null' == boundsCookie) ? [] : JSON2.parse(decodeURIComponent(boundsCookie));

            var locationCookie = Cookies.getCookie('SEARCH_LOCATION');
            _location = (!locationCookie || 'null' == locationCookie) ? [] : JSON2.parse(decodeURIComponent(locationCookie));

            var countryCodeCookie = Cookies.getCookie('SEARCH_COUNTRY_CODE');
            _countryCode = (!countryCodeCookie || 'null' == countryCodeCookie) ? [] : JSON2.parse(decodeURIComponent(countryCodeCookie));


            var formattedAddressCookie = Cookies.getCookie('SEARCH_FORMATTED_ADDRESS');
            _formattedAddress = (!formattedAddressCookie || 'null' == formattedAddressCookie) ? [] : JSON2.parse(decodeURIComponent(formattedAddressCookie));

        }
            //return true;//don't emit any event
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