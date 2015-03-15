/**
 * Created by dionis on 06/01/15.
 */

var React = require('react');
var assign = require('object-assign');

var Chats = require('./chats');
var ChatStore = require('../../../../shared/stores/ChatStore');
var ChatActions = require('../../../../shared/actions/ChatActions');

module.exports = React.createClass({
    getInitialState: function () {
        return {
            chats: ChatStore.getMyChats(),
            hasMoreSearchResults: []
        }
    },

    componentDidMount: function () {
        ChatStore.addChangeListener(this.myChatsListener);
        ChatActions.loadMyChats();
    },

    componentWillUnmount: function () {
        ChatStore.removeChangeListener(this.myChatsListener);
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

        return (
            <div className="col-sm-9 col-md-9 col-lg-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Сообщения</h4>


                        <form className="form-horizontal" role="form">
                            <div className="col-sm-offset-9 col-md-offset-9 col-lg-offset-9">
                                <a className="btn btn-primary center-block" href="#/user/chats/newMessage" >Новое сообщение</a>
                            </div>
                        </form>
                    </div>
                </div>

                <Chats items={items} shown={items.length > 0} hasMore={hasMoreResults} onHasMoreClicked={this.loadMoreResults} />
            </div>
        );
    }
});