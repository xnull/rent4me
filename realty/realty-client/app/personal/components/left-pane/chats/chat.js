/**
 * Created by dionis on 06/01/15.
 */

var React = require('react');
var assign = require('object-assign');
var Utils = require('rent4meUtil');
var _ = require('underscore');

var Chats = require('./chats');
var MessageThread = require('./ui/message-thread');
var ChatStore = require('../../../../shared/stores/ChatStore');
var ChatActions = require('../../../../shared/actions/ChatActions');

var UserStore = require('../../../../shared/stores/UserStore');
var UserActions = require('../../../../shared/actions/UserActions');

module.exports = React.createClass({
    getInitialState: function () {
        var chatKey = Utils.getQueryParams('id');
        console.log("chatKey=");
        console.log(chatKey);
        return {
            chatKey: chatKey,
            targetPersonId: Utils.getQueryParams('receiver_id'),
            messageText: null,
            messages: ChatStore.getChatMessages(chatKey),
            hasMoreSearchResults: [],
            me: UserStore.getMyProfile()
        }
    },

    componentDidMount: function () {
        ChatStore.addChatMessagesLoadedListener(this.myChatsListener);
        ChatStore.addNewConversationStartedListener(this.newMessageListener);
        ChatActions.loadChatMessages(this.state.chatKey);

        UserStore.addChangeListener(this.myUserListener);
        UserActions.loadMyProfileIfNotLoaded();
    },

    componentWillUnmount: function () {
        ChatStore.removeChatMessagesLoadedListener(this.myChatsListener);
        ChatStore.removeNewConversationStartedListener(this.newMessageListener);
        UserStore.removeChangeListener(this.myUserListener);
    },

    myChatsListener: function () {
        this.setState(assign(this.state, {
            messages: ChatStore.getChatMessages(this.state.chatKey)
        }));
    },

    newMessageListener: function () {
        this.setState(assign(this.state, {
            messageText: null
        }));
        ChatActions.loadChatMessages(this.state.chatKey);
    },

    myUserListener: function () {
        this.setState(assign(this.state, {
            me: UserStore.getMyProfile()
        }));
    },

    onMessageChange: function (e) {
        this.setState(assign(this.state, {
            messageText: e.target.value
        }));
    },

    onSendMessage: function () {
        ChatActions.sendNewMessage(this.state.targetPersonId, this.state.messageText);
    },

    render: function () {
        var items = this.state.messages || [];
        console.log('Chats:');
        console.log(items);
        var first = _.first(items) || {};
        console.log('First message:');
        console.log(first);
        var otherPerson = (first.receiver || {}).id == this.state.me.id ? first.sender : first.receiver;
        var message = this.state.messageText || [];
        var hasMoreResults = (this.state.hasMoreSearchResults || false) && false;//disable for now

        var maxHeight = 300;
        var maxHeightPX = maxHeight + 'px';

        var paddingTop = (maxHeight);
        var paddingTopPX = paddingTop + 'px';

        var messageFormStyle = {
            //paddingTop: paddingTopPX
        };

        return (
            <div className="col-sm-9 col-md-9 col-xs-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Чат с пользователем {(otherPerson || {}).name}</h4>


                        <MessageThread items={items} shown={items.length > 0} hasMore={hasMoreResults} onHasMoreClicked={this.loadMoreResults} maxHeight={maxHeightPX}/>


                        <br/>

                        <form className="form-horizontal" role="form" style={messageFormStyle}>
                            <div className="form-group">
                                <div className="col-sm-12 col-md-12 col-xs-12">
                                    <textarea rows="5" className="form-control" value={message} placeholder="Введите текст сообщения" onChange={this.onMessageChange}/>
                                </div>
                            </div>

                            <div className="col-md-offset-9 col-sm-offset-9 col-xs-offset-9 col-md-3 col-sm-3 col-xs-3">
                                <a className="btn btn-primary center-block" onClick={this.onSendMessage}>Отправить</a>
                            </div>

                            <br/>
                            <br/>


                        </form>

                    </div>
                </div>

            </div>
        );
    }
});