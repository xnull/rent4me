var AuthStore = require('../stores/AuthStore');
var SockJS = require('sockjs-client');
var WebSocketMultiplexer = require('./WebSocketMultiplexClient');

var _socket = null;
var _multiplexer = null;
var _recoveryCheckInterval = null;

module.exports = {
    getMultiplexer: function() {
        return _multiplexer;
    },

    init: function() {
        var self = this;
        var wsAuthMessage = AuthStore.getWSAuthMessage();
        if(wsAuthMessage) {
            _socket = new SockJS('/ws');
            _multiplexer = new WebSocketMultiplexer(_socket);

            _socket.onopen = function(){
                console.log('Connection open!');
                if(_recoveryCheckInterval) {
                    console.log('Clearing interval for recovery');
                    clearInterval(_recoveryCheckInterval);
                    _recoveryCheckInterval = null;
                }
                _socket.send(wsAuthMessage);
                //setConnected(true);
            };

            _socket.onclose = function(){
                console.log('Disconnecting connection');
                _socket = null;
                if(!_recoveryCheckInterval) {
                    console.log('Scheduling interval for recovery');
                    _recoveryCheckInterval = setInterval(function(){
                        self.init();//try to reconnect
                    }, 5000);
                }
            };

            _socket.onerror = function(err){
                console.log('Error occurred');
                if(_socket) {
                    _socket.close();
                }
            };

            _socket.onmessage = function (evt)
            {
                console.log('message received on socket!');
                console.log(evt.data);
                try {
                    var received_msg = JSON.parse(evt.data);
                } catch (e) {
                    console.error("Non-parseable message");
                }
                /*if(!received_msg.status) {
                 AppDispatcher.handleViewAction({
                 actionType: Constants.CHAT_MESSAGE_ADDED,
                 message: received_msg
                 });
                 }*/
                //showMessage(received_msg);
            };


        }
    }
};