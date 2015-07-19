/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var NavConstants = require('../constants/NavConstants');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');

var Ajax = require('../common/Ajax');

function navigateTo(path) {
    console.log('NavAction: Navigating to ' + path);

    AppDispatcher.handleViewAction({
        actionType: NavConstants.NAVIGATE_TO,
        page: path
    });
}

var NavActions = {
    /**
     */
    navigateToHome: function () {
        navigateTo('home');
    },

    navigateToLandlord: function () {
        navigateTo('landlord');
    },

    navigateToRenter: function () {
        navigateTo('renter');
    },

    navigateToSupport: function () {
        navigateTo('support');
    },

    navigateToChat: function () {
        navigateTo('chat');
    },

    navigateToCabinet: function () {
        navigateTo('cabinet');
    }
};

module.exports = NavActions;
