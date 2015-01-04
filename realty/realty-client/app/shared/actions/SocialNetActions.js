
/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var SocialNetConstants = require('../constants/SocialNetConstants');
var SocialNetStore = require('../stores/SocialNetStore');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');
var JSON = require('JSON2');
var Ajax = require('../common/Ajax');

var SocialNetActions = {
    resetSearchState: function(){
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_POSTS_RESET_SEARCH
        });
    },

    //bounds is google's: https://developers.google.com/maps/documentation/javascript/reference#LatLngBounds
    findPosts: function(text,withSubway) {
        BlockUI.blockUI();

        var limit = SocialNetStore.getLimit();
        var offset = SocialNetStore.getOffset();

        var url = '/rest/social/search?text=' + text + "&with_subway=" + withSubway + "&limit=" + limit + "&offset=" + offset;

        Ajax
            .GET(url)
            .authorized()
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: SocialNetConstants.SOCIAL_NET_POSTS_FOUND,
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
