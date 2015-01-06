/**
 * Created by dionis on 05/01/15.
 */
var React = require('react');

var _ = require('underscore');
var assign = require('object-assign');
var moment = require('moment');

var UserStore = require('../../../../shared/stores/UserStore');
var UserActions = require('../../../../shared/actions/UserActions');

var Chat = React.createClass({
    getInitialState: function() {
        return {
            me: UserStore.getMyProfile()
        };
    },

    componentWillMount: function() {
        UserStore.addChangeListener(this.meLoadListener);
        UserActions.loadMyProfileIfNotLoaded();
    },

    componentWillUnmount: function() {
        UserStore.removeChangeListener(this.meLoadListener);
    },

    meLoadListener: function() {
        this.setState(assign(this.state, {
            me: UserStore.getMyProfile()
        }));
    },

    render: function () {
        var item = this.props.item || {};
        var me = this.state.me;

        console.log("Me:");
        console.log(me);

        var targetPerson = item.receiver.id == me.id ? item.sender : item.receiver;

        return (
            <div className='panel'>
                <a href="#" className="list-group-item">

                    <p className="list-group-item-text">
                        <h4>{targetPerson.name} </h4>

                        <div className="row">
                            <div className="col-md-12">
                            {item.message}
                            </div>
                        </div>

                        <hr/>

                        <div className="row">
                            <div className="col-md-6">
                                Добавлено: {moment(item.created).format("lll")}
                            </div>
                            <div className="col-md-6">
                                Обновлено: {moment(item.updated).format("lll")}
                            </div>
                        </div>

                    </p>
                    <hr/>
                </a>
                <br/>
            </div>
        );
    }
});

var Chats = React.createClass({

    render: function () {
        var shown = this.props.shown || false;
        var items = this.props.items || [];
        var hasMore = this.props.hasMore || false;

        console.log("Has more?" + hasMore);

        var onHasMoreClicked = this.props.onHasMoreClicked;

        var hasMoreElement = hasMore ?
            (
                <a href="javascript:none;" onClick={onHasMoreClicked} className="list-group-item">

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
                <Chat item={item}/>
            );
        });

        return (
            <div className="col-md-9" style={style}>
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