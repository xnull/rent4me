var React = require('react');
var Ads = require('./ads');
var SocialNet = require('./socialnet/socialnet');

var Utils = require('../../../shared/common/Utils');
var UserStore = require('../../../shared/stores/UserStore');
var NotificationStore = require('../../../shared/stores/notification-store');
var NotificationConstants = require('../../../shared/constants/notification-constants');
var AuthStore = require('../../../shared/stores/AuthStore');
var UserActions = require('../../../shared/actions/UserActions');

var assign = require('object-assign');

var UserPanel = React.createClass({
    getInitialState: function () {
        return {
            me: UserStore.getMyProfile(),
            countOfNewMessages: NotificationStore.countOfUnreadNotifications(NotificationConstants.NOTIFICATION_TYPE_NEW_MESSAGE)
        };
    },

    componentDidMount: function () {
        UserStore.addChangeListener(this._onChange);
        NotificationStore.addChangeListener(this._onChangeNotifications);

        UserActions.loadMyProfileIfNotLoaded();
    },

    componentWillUnmount: function () {
        UserStore.removeChangeListener(this._onChange);
        NotificationStore.removeChangeListener(this._onChangeNotifications);
    },

    _onChange: function (event) {
        this.setState(assign({}, this.state, {me: UserStore.getMyProfile()}));
    },

    _onChangeNotifications: function (event) {
        this.setState(assign({}, this.state, {countOfNewMessages: NotificationStore.countOfUnreadNotifications(NotificationConstants.NOTIFICATION_TYPE_NEW_MESSAGE)}));
    },

    render: function () {
        var authorized = AuthStore.hasCredentials();

        var style = authorized ? {} : {display: 'none'};

        var me = this.state.me || {};

        var newMessagesCountDisplay = this.state.countOfNewMessages > 0 ? (<b className="badge badge-notification pull-right">{this.state.countOfNewMessages}</b>) : null;

        return (
            <div className="panel panel-default" style={style}>
                <div className="panel-body">
                    <h4>Пользователь</h4>

                    <div className="well well-sm">
                        <div className="media">
                            <a className="thumbnail pull-left" href="#">
                                <img className="media-object" width="80" style={{height: 60}}/>
                            </a>

                            <div className="media-body">
                                <h4 className="media-heading">{me.name}</h4>

                                <br/>

                                <div className="col-sm-12 col-md-12 col-xs-12">
                                    <a className="btn btn-default center-block" href="#/user">Настройки</a>
                                </div>

                            </div>

                            <br/>

                            <div className="col-sm-12 col-md-12 col-xs-12">
                                <a className="btn btn-default center-block" href="#/user/chats">
                                    <b className="glyphicon glyphicon-envelope pull-left"></b>
                                    Сообщения {newMessagesCountDisplay}
                                </a>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        );
    }
});

module.exports = React.createClass({
    render: function () {
        return (
            <div>
                <UserPanel/>
            </div>
        )
    }
});
