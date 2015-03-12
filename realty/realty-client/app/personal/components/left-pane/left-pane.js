var React = require('react');
var PersonalCabinetComponent = require('./personal.js');
var NewsComponent = require('./news.js');
var LegalComponent = require('./legal.js');
var SocialNet = require('./socialnet/socialnet');

var Utils = require('../../../shared/common/Utils');
var UserStore = require('../../../shared/stores/UserStore');
var AuthStore = require('../../../shared/stores/AuthStore');
var UserActions = require('../../../shared/actions/UserActions');

var assign = require('object-assign');

var UserPanel = React.createClass({
    getInitialState: function () {
        return UserStore.getMyProfile();
    },

    componentDidMount: function () {
        UserStore.addChangeListener(this._onChange);

        UserActions.loadMyProfileIfNotLoaded();
    },

    componentWillUnmount: function () {
        UserStore.removeChangeListener(this._onChange);
    },

    _onChange: function (event) {
        this.setState(UserStore.getMyProfile());
    },

    render: function () {
        var authorized = AuthStore.hasCredentials();

        var style = authorized ? {} : {display: 'none'};

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
                                <h4 className="media-heading">{this.state.name}</h4>

                                <br/>

                                <div className="col-md-12">
                                    <a className="btn btn-default center-block" href="#/user">Настройки</a>
                                </div>

                            </div>

                            <br/>

                            <div className="col-md-12">
                                <a className="btn btn-default center-block" href="#/user/chats">
                                    <b className="glyphicon glyphicon-envelope pull-left"></b> Сообщения <b className="badge pull-right">3</b>
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
            <div className="col-md-3">
                <UserPanel/>
                <LegalComponent/>
            </div>
        )
    }
});
