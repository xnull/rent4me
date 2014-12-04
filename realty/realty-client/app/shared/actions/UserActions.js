/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var UserConstants = require('../constants/UserConstants');

var UserActions = {
    /**
     * @param {object} obj
     */
    create: function (obj) {
        AppDispatcher.handleViewAction({
            actionType: UserConstants.USER_CREATE,
            user: obj
        });
    }
};

module.exports = UserActions;
