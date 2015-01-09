/**
 * Created by dionis on 06/01/15.
 */

var React = require('react');
var assign = require('object-assign');
var Utils = require('rent4meUtil');
var _ = require('underscore');

var Chats = require('./chats');
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

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Чат с пользователем {(otherPerson || {}).name}</h4>


                        <form className="form-horizontal" role="form">
                            <div className="form-group">
                                <label className="col-md-2 control-label">Сообщение</label>
                                <div className="col-md-6">
                                    <textarea rows="7" className="form-control" value={message} placeholder="Введите текст сообщения" onChange={this.onMessageChange}/>
                                </div>
                            </div>

                            <div className="col-md-offset-5 col-md-3">
                                <a className="btn btn-primary center-block" onClick={this.onSendMessage}>Отправить</a>
                            </div>

                            <br/>
                            <br/>


                        </form>

                        <p>
                            <Chats items={items} shown={items.length > 0} hasMore={hasMoreResults} onHasMoreClicked={this.loadMoreResults} />
                        </p>
                    </div>
                </div>

            </div>
        );
    }
});