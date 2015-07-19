/**
 * Created by dionis on 06/01/15.
 */

var React = require('react');
var assign = require('object-assign');

var Chats = require('./chats');
var ChatStore = require('../../../../shared/stores/ChatStore');
var ChatActions = require('../../../../shared/actions/ChatActions');

var NavActions = require('../../../../shared/actions/NavActions');
var AuthStore = require('../../../../shared/stores/AuthStore');

module.exports = React.createClass({
    getInitialState: function () {
        return {
            chats: ChatStore.getMyChats(),
            hasMoreSearchResults: []
        }
    },

    componentDidMount: function () {
        NavActions.navigateToChat();
        AuthStore.addChangeListener(this._onAuthChange);

        ChatStore.addChangeListener(this.myChatsListener);
        ChatActions.loadMyChats();
    },

    componentWillUnmount: function () {
        AuthStore.removeChangeListener(this._onAuthChange);
        ChatStore.removeChangeListener(this.myChatsListener);
    },

    _onAuthChange: function() {
        this.setState(assign({}, this.state, {
            isAuthorized: AuthStore.hasCredentials()
        }));
    },

    myChatsListener: function () {
        this.setState(assign(this.state, {
            chats: ChatStore.getMyChats()
        }));
    },

    render: function () {
        var items = this.state.chats || [];
        console.log('Chats:');
        console.log(items);
        var hasMoreResults = (this.state.hasMoreSearchResults || false) && false;//disable for now

        var newMessageButtonButton = (<form className="form-horizontal" role="form">
            <div className="col-sm-offset-9 col-md-offset-9 col-xs-offset-9">
                <a className="btn btn-primary center-block" href="#/user/chats/newMessage" >Новое сообщение</a>
            </div>
        </form>);

        //disable temporarily new message button
        newMessageButtonButton = null;

        return (
            <div>
                <div className="panel">

                    <div className="panel-body">
                        <h4>Сообщения</h4>

                        {newMessageButtonButton}

                    </div>
                </div>

                <Chats items={items} shown={items.length > 0} hasMore={hasMoreResults} onHasMoreClicked={this.loadMoreResults} />
            </div>
        );
    }
});