/**
 * Created by dionis on 05/01/15.
 */
var React = require('react');

var _ = require('underscore');
var assign = require('object-assign');
var moment = require('moment');

var UserStore = require('../../../../shared/stores/UserStore');
var UserActions = require('../../../../shared/actions/UserActions');
var NotificationStore = require('../../../../shared/stores/notification-store');
var NotificationConstants = require('../../../../shared/constants/notification-constants');

var Chat = React.createClass({
    getInitialState: function () {
        return {
            me: UserStore.getMyProfile()
        };
    },

    componentDidMount: function () {
        UserStore.addChangeListener(this.meLoadListener);
        UserActions.loadMyProfileIfNotLoaded();
    },

    componentWillUnmount: function () {
        UserStore.removeChangeListener(this.meLoadListener);
    },

    meLoadListener: function () {
        this.setState(assign(this.state, {
            me: UserStore.getMyProfile()
        }));
    },

    render: function () {
        var item = this.props.item || {};
        var me = this.state.me;
        var newCount = this.props.newCount || 0;

        var newMessagesCountDisplay = newCount > 0 ? (<span><b className="badge badge-notification">{newCount}</b><br/></span>) : null;

        console.log("Me:");
        console.log(me);

        var targetPerson = item.receiver.id == me.id ? item.sender : item.receiver;

        var url = "#/user/chat?id=" + item.chat_key + "&receiver_id=" + targetPerson.id;

        return (
            <div className='panel'>
                <a href={url} className="list-group-item">

                    <p className="list-group-item-text">
                        <div className="row">
                            <div className="col-sm-2 col-md-2 col-xs-2">
                                {newMessagesCountDisplay}
                                <strong>{targetPerson.name}</strong>
                                <br/>
                                <small>{moment(item.created).format("lll")}</small>
                            </div>
                            <div className="col-sm-10 col-md-10 col-xs-10">
                                <strong>{item.sender.name}</strong>
                                : {item.message}
                            </div>
                        </div>
                    </p>
                </a>
                <br/>
            </div>
        );
    }
});

var Chats = React.createClass({

    getInitialState: function () {
        return {
            unreadMessageNotifications: NotificationStore.listUnreadNotifications(NotificationConstants.NOTIFICATION_TYPE_NEW_MESSAGE)
        };
    },

    componentDidMount: function () {
        NotificationStore.addChangeListener(this._onChangeNotifications);
    },

    componentWillUnmount: function () {
        NotificationStore.removeChangeListener(this._onChangeNotifications);
    },

    _onChangeNotifications: function () {
        this.setState(assign({}, this.state, {unreadMessageNotifications: NotificationStore.listUnreadNotifications(NotificationConstants.NOTIFICATION_TYPE_NEW_MESSAGE)}));
    },

    render: function () {
        var shown = this.props.shown || false;
        var items = this.props.items || [];
        var hasMore = this.props.hasMore || false;

        console.log("Has more?" + hasMore);

        var onHasMoreClicked = this.props.onHasMoreClicked;


        var unreadNotificationsByChatCount = {};
        (this.state.unreadMessageNotifications || {}).forEach(nf=>{
            if(!unreadNotificationsByChatCount[nf.value.chat_key]) {
                unreadNotificationsByChatCount[nf.value.chat_key] = 1;
            } else {
                unreadNotificationsByChatCount[nf.value.chat_key] = unreadNotificationsByChatCount[nf.value.chat_key] + 1;
            }
        });

        var hasMoreElement = hasMore ?
            (
                <a href="javascript:void(0)" onClick={onHasMoreClicked} className="list-group-item">

                    <p className="list-group-item-text">
                        Загрузить еще
                    </p>

                </a>
            ) : null;

        var style = {};
        if (!shown) {
            style['display'] = 'none';
        }

        var chats = items.map(function (item) {
            return (
                <Chat item={item} newCount={unreadNotificationsByChatCount[item.chat_key]}/>
            );
        });

        return (
            <div style={style}>
                <div className="panel panel-default">
                    <div className="panel-heading">
                        <h4>Беседы</h4>
                    </div>

                    <div className="panel-body">

                        <div className="bs-component">
                            <div className="list-group">

                            {chats}

                                <br/>

                            {hasMoreElement}

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = Chats;