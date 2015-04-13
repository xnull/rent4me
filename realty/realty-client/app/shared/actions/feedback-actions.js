/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');

var AuthStore = require('../stores/AuthStore');

var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');
var JSON = require('JSON2');
var Ajax = require('../common/Ajax');

var _sendFeedbackInProgress = false;

var Actions = {
        sendFeedback: function (name, email, text) {
        if(!_sendFeedbackInProgress) {
            _sendFeedbackInProgress = true;

            BlockUI.blockUI();

            var authorized = AuthStore.hasCredentials();

            var url = '/rest/feedback';

            var data = {
                name: name,
                email: email,
                text: text
            };

            var builder = Ajax
                .POST(url);

            if(authorized) {
                builder = builder.authorized();
            }


            builder.withJsonBody(data)
            .onSuccess(function (out) {
                BlockUI.unblockUI();
                _sendFeedbackInProgress = false;
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
                _sendFeedbackInProgress = false;
            })
            .execute();
        }
    }
};

module.exports = Actions;
