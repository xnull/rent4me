/**
 * Created by dionis on 04/12/14.
 */
var AppDispatcher = require('../dispatcher/AppDispatcher');

var EventEmitter = require('events').EventEmitter;
var Constants = require('../constants/ChatConstants');

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

var _myChats = [];
var _chatMessages = [];

var _offset = 0;
var _limit = 30;
var _hasMoreResults = false;


var CHAT_CHANGE_EVENT = 'chat_change';
var CHAT_NEW_CONVERSATION_STARTED_EVENT = 'chat_new_conv_started';
var CHAT_MESSAGES_LOADED_EVENT = 'chat_msgs_loaded';

var Store = assign({}, EventEmitter.prototype, {
    emitChange: function() {
        this.emit(CHAT_CHANGE_EVENT);
    },

    emitNewConversationStarted: function() {
        this.emit(CHAT_NEW_CONVERSATION_STARTED_EVENT);
    },

    emitChatMessagesLoadedEvent: function() {
        this.emit(CHAT_MESSAGES_LOADED_EVENT);
    },


    saveChats: function(chats) {
        console.log('Saving chats:');
        var len = chats.length;
        console.log('Len: '+len);
        //_hasMoreResults = len == _limit;
        console.log('Has more results?: '+_hasMoreResults);
        //_offset += len;
        console.log('New offset: '+_offset);
        //_chats = _chats.concat(chats);
        _myChats = chats;
        console.log('Chats in cache: ');
        console.log(_myChats);
    },

    getMyChats: function() {
        return _myChats;
    },

    saveChatMessages: function(chatKey, messages) {
        _chatMessages[chatKey] = messages || [];
    },

    getChatMessages: function(chatKey) {
        return _chatMessages[chatKey] || [];
    },

    //hasMoreSearchResults: function() {
    //    return _hasMoreResults;
    //},

    /**
     * @param {function} callback
     */
    addChangeListener: function(callback) {
        this.on(CHAT_CHANGE_EVENT, callback);
    },
    /**
     * @param {function} callback
     */
    removeChangeListener: function(callback) {
        this.removeListener(CHAT_CHANGE_EVENT, callback);
    },

    /**
     * @param {function} callback
     */
    addNewConversationStartedListener: function(callback) {
        this.on(CHAT_NEW_CONVERSATION_STARTED_EVENT, callback);
    },
    /**
     * @param {function} callback
     */
    removeNewConversationStartedListener: function(callback) {
        this.removeListener(CHAT_NEW_CONVERSATION_STARTED_EVENT, callback);
    },

    /**
     * @param {function} callback
     */
    addChatMessagesLoadedListener: function(callback) {
        this.on(CHAT_MESSAGES_LOADED_EVENT, callback);
    },
    /**
     * @param {function} callback
     */
    removeChatMessagesLoadedListener: function(callback) {
        this.removeListener(CHAT_MESSAGES_LOADED_EVENT, callback);
    }


});

// Register to handle all updates
AppDispatcher.register(function(payload){
    var action = payload.action;
//    console.log('Apartment store payload received in dispatcher');
//    var apartmentObject = assign({}, action.apartment || {});
//    console.log("Action:");
//    console.log(action);
//    console.log("Apartment:");
//    console.log(apartmentObject);

    switch(action.actionType) {
        case Constants.CHAT_LIST_LOADED:
            console.log('found posts:');
            console.log(action.chats);
            Store.saveChats(action.chats || []);
            break;


        case Constants.CHAT_NEW_CONVERSATION_STARTED:
            console.log('new conversation started with message:');
            console.log(action.newMessage);

            Store.emitNewConversationStarted();
            return true;
            break;

        case Constants.CHAT_MESSAGES_LOADED:
            console.log('chat messages loaded for chat '+action.chatKey+':');
            console.log(action.messages);

            Store.saveChatMessages(action.chatKey, action.messages || []);

            Store.emitChatMessagesLoadedEvent();
            return true;
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