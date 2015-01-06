/**
 * Created by dionis on 06/01/15.
 */

var React = require('react');
var assign = require('object-assign');
var Ajax = require('rent4meAjax');
var _ = require('underscore');
var BlockUI = require('rent4meBlockUI');

var ReactAutocomplete = require('rent4meAutocomplete');

var ChatStore = require('../../../../shared/stores/ChatStore');
var ChatActions = require('../../../../shared/actions/ChatActions');

var UserStore = require('../../../../shared/stores/UserStore');
var UserActions = require('../../../../shared/actions/UserActions');

module.exports = React.createClass({
    getInitialState: function() {
        return {
            messageText: null,
            targetPersonId: null,
            me: UserStore.getMyProfile()
        }
    },

    componentWillMount: function() {
        ChatStore.addNewConversationStartedListener(this.newConversationStartedListener);
        UserStore.addChangeListener(this.myUserListener);
        UserActions.loadMyProfileIfNotLoaded();
    },

    componentWillUnmount: function() {
        ChatStore.removeNewConversationStartedListener(this.newConversationStartedListener);
        UserStore.removeChangeListener(this.myUserListener);
    },

    newConversationStartedListener: function() {
        console.log('New conversation started listener called');
        this.setState(assign(this.state, {
            messageText: null,
            targetPersonId: null
        }));
        //TODO: redirect to proper thread. For now simply redirect to store
        document.location.href = '#/user/chats';
    },

    myUserListener: function() {
        this.setState(assign(this.state, {
            me: UserStore.getMyProfile()
        }));
    },

    onMessageChange: function(e) {
        this.setState(assign(this.state, {
            messageText: e.target.value
        }));
    },

    onTargetPersonChanged: function(item) {
        this.setState(assign(this.state, {
            targetPersonId: item.id
        }));
    },

    onSendMessage: function() {
        ChatActions.startNewConversation(this.state.targetPersonId, this.state.messageText);
    },

    _searchRemote: function(options, searchTerm, cb) {
        var that = this;
        Ajax
            .GET('/rest/users/find?name='+searchTerm)
            .authorized()
            .onSuccess(function (data) {
                cb(null, _.filter(_.map(data, function(item){
                    return {id: item.id, title: item.name};
                }), function(item) {
                    return item.id != that.state.me.id;
                }));
            })
            .onError(function (xhr, status, err) {
            })
            .execute();
    },

    render: function () {
        var message = this.state.messageText;
        var targetPersonId = this.state.targetPersonId;

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Новое сообщение</h4>


                        <form className="form-horizontal" role="form">
                            <div className="form-group">
                                <label className="col-md-2 control-label">ID пользователя</label>
                                <div className="col-md-10">
                                    <ReactAutocomplete search={this._searchRemote} onChange={this.onTargetPersonChanged}/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="col-md-2 control-label">Сообщение</label>
                                <div className="col-md-6">
                                    <textarea rows="7" className="form-control" value={message} placeholder="Введите текст сообщения" onChange={this.onMessageChange}/>
                                </div>
                            </div>

                            <div className="col-md-offset-5 col-md-3">
                                <a className="btn btn-primary center-block" onClick={this.onSendMessage}>Отправить</a>
                            </div>
                        </form>
                    </div>
                </div>

            </div>
        );
    }
});