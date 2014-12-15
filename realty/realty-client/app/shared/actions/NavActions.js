/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var NavConstants = require('../constants/NavConstants');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');

var Ajax = require('../common/Ajax');

var NavActions = {
    /**
     */
    navigateToHome: function () {
        console.log('NavAction: Navigating to home');
        AppDispatcher.handleViewAction({
            actionType: NavConstants.NAVIGATE_TO,
            page: 'home'
        });
    },

    navigateToLandlord: function () {
        console.log('NavAction: Navigating to landlord');
        AppDispatcher.handleViewAction({
            actionType: NavConstants.NAVIGATE_TO,
            page: 'landlord'
        });
    }
};

module.exports = NavActions;
