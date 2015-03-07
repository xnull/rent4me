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

    resetFBSearchState: function () {
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_POSTS_RESET_SEARCH
        });
    },

    changeFBSearchText: function (text) {
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_TEXT,
            text: text
        });
    },

    changeFBSearchRooms: function (oneAptSelected, twoAptSelected, threeAptSelected) {
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_ROOMS,
            value: {
                '1': oneAptSelected || false,
                '2': twoAptSelected || false,
                '3': threeAptSelected || false
            }
        });
    },

    _trimString: function (str) {
        var what = ['.', ',', ' '];
        var len = what.length;
        var i;

        str = str || '';

        console.log('Trimmed value: "' + str + '"');

        for (i = 0; i < len; i++) {
            var itemToReplace = what[i];
            console.log('Removing "' + itemToReplace + '" value in: "' + str + '"');
            str = str.replace(itemToReplace, '');
            console.log('Replaced value: "' + str + '"');
        }

        return str;
    },

    changeFBSearchPrice: function (min, max) {

        min = this._trimString(min);
        max = this._trimString(max);

        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_PRICE,
            value: {
                min: min ? min : null,
                max: max ? max : null
            }
        });
    },

    changeFBSearchWithSubway: function (value) {
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_WITH_SUBWAY,
            value: value
        });
    },

    changeFBSearchType: function (value) {
        AppDispatcher.handleViewAction({
            actionType: SocialNetConstants.SOCIAL_NET_POSTS_SAVE_SEARCH_TYPE,
            value: value
        });
    },

    //bounds is google's: https://developers.google.com/maps/documentation/javascript/reference#LatLngBounds
    findPosts: function (text, type, withSubway, oneRoomAptSelected, twoRoomAptSelected, threeRoomAptSelected, minPrice, maxPrice) {
        BlockUI.blockUI();

        var limit = SocialNetStore.getLimit();
        var offset = SocialNetStore.getOffset();
        //TODO: refactor & unify.
        var url = '/rest/apartments/search?text=' + (text ? text : '') + "&type=" + type + "&with_subway=" + (withSubway ? true : false) + "&limit=" + limit + "&offset=" + offset;

        if (oneRoomAptSelected) {
            url += "&rooms=1";
        }

        if (twoRoomAptSelected) {
            url += "&rooms=2";
        }

        if (threeRoomAptSelected) {
            url += "&rooms=3";
        }

        console.log('Min price before trimming' + minPrice);
        console.log('Max price before trimming' + maxPrice);

        minPrice = this._trimString(minPrice);
        maxPrice = this._trimString(maxPrice);

        console.log('Min price after trimming' + minPrice);
        console.log('Max price after trimming' + maxPrice);


        if (minPrice) {
            url += "&min_price=" + minPrice;
        }

        if (maxPrice) {
            url += "&max_price=" + maxPrice;
        }


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
