var events = require('events');
events.EventEmitter.prototype._maxListeners = 100;
/**
 * Created by dionis on 08/01/15.
 */
var EventEmitter = events.EventEmitter;

module.exports = EventEmitter;