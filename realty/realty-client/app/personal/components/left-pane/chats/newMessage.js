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
    getInitialState: function () {
        return {
            messageText: null,
            targetPersonId: null,
            me: UserStore.getMyProfile()
        }
    },

    componentDidMount: function () {
        ChatStore.addNewConversationStartedListener(this.newConversationStartedListener);
        UserStore.addChangeListener(this.myUserListener);
        UserActions.loadMyProfileIfNotLoaded();
    },

    componentWillUnmount: function () {
        ChatStore.removeNewConversationStartedListener(this.newConversationStartedListener);
        UserStore.removeChangeListener(this.myUserListener);
    },

    newConversationStartedListener: function () {
        var myId = this.state.me.id;
        var otherId = this.state.targetPersonId;
        var min = Math.min(myId, otherId);
        var max = Math.max(myId, otherId);
        var chatKey = min + "_" + max;
        console.log('New conversation started listener called');
        this.setState(assign(this.state, {
            messageText: null,
            targetPersonId: null
        }));
        document.location.href = '#/user/chat?id=' + chatKey + "&receiver_id=" + otherId;
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

    onTargetPersonChanged: function (item) {
        this.setState(assign(this.state, {
            targetPersonId: item.id
        }));
    },

    onSendMessage: function () {
        ChatActions.sendNewMessage(this.state.targetPersonId, this.state.messageText);
    },

    _searchRemote: function (options, searchTerm, cb) {
        var that = this;
        Ajax
            .GET('/rest/users/find?name=' + searchTerm)
            .authorized()
            .onSuccess(function (data) {
                cb(null, _.filter(_.map(data, function (item) {
                    return {id: item.id, title: item.name};
                }), function (item) {
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
            <div className="panel">

                <div className="panel-body">
                    <h4>Новое сообщение</h4>


                    <form className="form-horizontal" role="form">
                        <div className="form-group">
                            <label className="col-sm-2 col-md-2 col-xs-2 control-label">ID пользователя</label>
                            <div className="col-sm-6 col-md-6 col-xs-6">
                                <ReactAutocomplete search={this._searchRemote} onChange={this.onTargetPersonChanged}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-sm-2 col-md-2 col-xs-2 control-label">Сообщение</label>
                            <div className="col-sm-6 col-md-6 col-xs-6">
                                <textarea rows="7" className="form-control" value={message} placeholder="Введите текст сообщения" onChange={this.onMessageChange}/>
                            </div>
                        </div>

                        <div className="col-sm-offset-5 col-md-offset-5 col-xs-offset-5 col-sm-3 col-md-3 col-xs-3">
                            <a className="btn btn-primary center-block" onClick={this.onSendMessage}>Отправить</a>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
});