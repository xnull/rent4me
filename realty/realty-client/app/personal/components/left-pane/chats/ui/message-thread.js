/**
 * Created by dionis on 05/01/15.
 */
var React = require('react');

var _ = require('underscore');
var assign = require('object-assign');
var moment = require('moment');

var UserStore = require('../../../../../shared/stores/UserStore');
var UserActions = require('../../../../../shared/actions/UserActions');

var ChatActions = require('../../../../../shared/actions/ChatActions');

var Message = React.createClass({
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

        var isMe = item.receiver.id == me.id;
        var name = item.sender.name;

        var style = {
            paddingTop: '10px'
        };

        return (
            <div>
                <div className="col-sm-12 col-md-12 col-xs-12" style={style}>
                    <div className="col-md-10 col-sm-10 col-xs-10">
                        <strong>
                            {name}
                        </strong>
                        <br/>
                        {item.message}
                    </div>
                    <div className="col-md-2 col-sm-2 col-xs-2">
                        <small>{moment(item.created).format("lll")}</small>
                    </div>
                    <br/>
                    <div className="col-sm-12 col-md-12 col-xs-12">
                        <hr/>
                    </div>
                </div>
            </div>
        );
    }
});

var MessageThread = React.createClass({
    getInitialState: function () {
        return {
            componentHeight: 0
        };
    },

    componentDidUpdate: function () {
        this.scrollDown();
    },

    scrollDown: function () {
        var messageContainer = $("#messageContainer");
        console.log('Message container:');
        console.log(messageContainer);
        var container = messageContainer[0];
        if (container) {
            var scrollHeight = container.scrollHeight;
            if (scrollHeight != this.state.componentHeight) {
                this.setState(assign(this.state, {
                    componentHeight: scrollHeight
                }));

                console.log('Message container scroll height:' + scrollHeight);
                messageContainer.scrollTop(scrollHeight);
            } else {
                console.log('nothing changed for scroll div.');
            }
        } else {
            console.log('no container for message thread');
        }
    },

    render: function () {
        var shown = this.props.shown || false;
        var items = this.props.items || [];
        var hasMore = this.props.hasMore || false;

        //this.scrollDown();

        console.log("Has more?" + hasMore);

        var onHasMoreClicked = this.props.onHasMoreClicked;

        var hasMoreElement = hasMore ?
            (
                <a href="javascript:void(0)" onClick={onHasMoreClicked} className="list-group-item">

                    <p className="list-group-item-text">
                        Загрузить еще
                    </p>

                </a>
            ) : null;

        var height = this.props.maxHeight || '100px';

        console.log('Max height: ' + height);

        var style = {
            height: height
        };

        var reversedItems = items.slice();
        reversedItems.reverse();//perform copy

        var messageElements = reversedItems.map(function (item) {
            return (
                <Message item={item}/>
            );
        });

        var newStyle = {
            overflowY: 'auto'
        }

        return (

            <div className="panel panel-default" style={newStyle} id="messageContainer">
                <div className="col-sm-12 col-md-12 col-xs-12" style={style} >
                    <div className="panel-body" >
                        <div className="list-group">

                        {hasMoreElement}

                            <br/>

                        {messageElements}

                        </div>
                    </div>
                </div>
            </div>

        );
    }
});

module.exports = MessageThread;