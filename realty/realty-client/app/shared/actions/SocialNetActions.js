
/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var SocialNetConstants = require('../constants/SocialNetConstants');
var SocialNetRenterStore = require('../stores/SocialNetRenterStore');
var SocialNetLessorStore = require('../stores/SocialNetLessorStore');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');
var JSON = require('JSON2');
var Ajax = require('../common/Ajax');

var SocialNetActions = {
    resetRenterSearchState: function(){
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_RENTER_POSTS_RESET_SEARCH
        });
    },

    resetLessorSearchState: function(){
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_LESSOR_POSTS_RESET_SEARCH
        });
    },

    changeRenterSearchText: function(text){
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_RENTER_POSTS_SAVE_SEARCH_TEXT,
            text: text
        });
    },

    changeLessorSearchText: function(text){
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_LESSOR_POSTS_SAVE_SEARCH_TEXT,
            text: text
        });
    },

    changeRenterSearchWithSubway: function(value){
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_RENTER_POSTS_SAVE_SEARCH_WITH_SUBWAY,
            value: value
        });
    },

    changeLessorSearchWithSubway: function(value){
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_LESSOR_POSTS_SAVE_SEARCH_WITH_SUBWAY,
            value: value
        });
    },

    //bounds is google's: https://developers.google.com/maps/documentation/javascript/reference#LatLngBounds
    findRenterPosts: function(text,withSubway) {
        BlockUI.blockUI();

        var limit = SocialNetRenterStore.getLimit();
        var offset = SocialNetRenterStore.getOffset();

        var url = '/rest/social/renter/search?text=' + text + "&with_subway=" + withSubway + "&limit=" + limit + "&offset=" + offset;

        Ajax
            .GET(url)
            .authorized()
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: SocialNetConstants.SOCIAL_NET_RENTER_POSTS_FOUND,
                    posts: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    },

    //bounds is google's: https://developers.google.com/maps/documentation/javascript/reference#LatLngBounds
    findLessorPosts: function(text,withSubway) {
        BlockUI.blockUI();

        var limit = SocialNetLessorStore.getLimit();
        var offset = SocialNetLessorStore.getOffset();

        var url = '/rest/social/lessor/search?text=' + text + "&with_subway=" + withSubway + "&limit=" + limit + "&offset=" + offset;

        Ajax
            .GET(url)
            .authorized()
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: SocialNetConstants.SOCIAL_NET_LESSOR_POSTS_FOUND,
                    posts: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    }
};

module.exports = SocialNetActions;
