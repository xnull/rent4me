/**
 * Created by dionis on 04/12/14.
 */
var keyMirror = require('react/lib/keyMirror');
var Constants = keyMirror({
    NEW_NOTIFICATION_RECEIVED: null,
    NOTIFICATIONS_LOADED: null,
    NOTIFICATIONS_RESOLVED: null,
    NOTIFICATIONS_AUTORESOLVE_FOR_CHAT: null
});
Constants.NOTIFICATION_TYPE_NEW_MESSAGE = 1;
module.exports = Constants;