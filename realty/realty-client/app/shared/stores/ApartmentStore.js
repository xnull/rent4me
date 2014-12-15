/**
 * Created by dionis on 04/12/14.
 */
var AppDispatcher = require('../dispatcher/AppDispatcher');

var EventEmitter = require('events').EventEmitter;
var ApartmentConstants = require('../constants/ApartmentConstants');

var assign = require('object-assign');

function emptyApartment() {
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

var _me = emptyApartment();

var _apartments = [];
var _offset = 0;
var _limit = 100;
var _hasMoreResults = false;


var CHANGE_EVENT = 'change';

var saveMyProfile = function(myProfile){
    //copy props
    _me = assign({}, _me, myProfile);
};

var getMyProfile = function() {
    //copy props
    return assign({}, _me);
};

var ApartmentStore = assign({}, EventEmitter.prototype, {
    emitChange: function() {
        this.emit(CHANGE_EVENT);
    },

    emptyApartment: function() {
        return emptyApartment();
    },

    getMyProfile: function() {
        return getMyProfile();
    },

    saveMyProfile: function(myProfile) {
        saveMyProfile(myProfile);
    },

    saveSearchResults: function(apartments) {
        console.log('Saving search results:');
        var len = apartments.length;
        console.log('Len: '+len);
        _hasMoreResults = len == _limit;
        console.log('Has more results?: '+_hasMoreResults);
        _offset += len;
        console.log('New offset: '+_offset);
        _apartments = _apartments.concat(apartments);
        console.log('Apartments in cache: ');
        console.log(_apartments);
    },

    getSearchResults: function() {
        return _apartments;
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
        case ApartmentConstants.APARTMENT_CREATE:
//            console.log("case: Apartment save");
            saveMyProfile(apartmentObject);
            break;
        case ApartmentConstants.APARTMENT_UPDATE:
//            console.log("case: Apartment save");
            saveMyProfile(apartmentObject);
            break;
        case ApartmentConstants.APARTMENT_LOADED:
//            console.log("case: Apartment loaded");
            saveMyProfile(apartmentObject);
            break;
        case ApartmentConstants.APARTMENT_DESTROY:
//            console.log("case: Apartment destroy");
            var newProfileForDeletion = ApartmentStore.emptyApartment();
//            console.log("New profile for delete:");
//            console.log(newProfileForDeletion);
//            saveMyProfile(newProfileForDeletion);
            _me = newProfileForDeletion;
            break;
        case ApartmentConstants.APARTMENT_CHANGE_REQUEST_CREATED:
            break;

        case ApartmentConstants.APARTMENTS_FOUND:
            console.log('found apartments:');
            console.log(action.apartments);
            ApartmentStore.saveSearchResults(action.apartments || []);
            break;

        case ApartmentConstants.APARTMENTS_RESET_SEARCH:
            _apartments = [];
            _hasMoreResults = false;
            _offset = 0;
            break;
        default:
//            console.log("case: default");
            return false;
    }


    // This often goes in each case that should trigger a UI change. This store
    // needs to trigger a UI change after every view action, so we can make the
    // code less repetitive by putting it here. We need the default case,
    // however, to make sure this only gets called after one of the cases above.
    ApartmentStore.emitChange();
    return true; // No errors. Needed by promise in Dispatcher.
});

module.exports = ApartmentStore;