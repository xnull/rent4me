var React = require('react');
var Router = require('react-router');

var Route = Router.Route;
var NotFoundRoute = Router.NotFoundRoute;
var DefaultRoute = Router.DefaultRoute;
var Link = Router.Link;
var RouteHandler = Router.RouteHandler;

var UserActions = require('../../shared/actions/UserActions');

var AuthActions = require('../../shared/actions/AuthActions');
var SocialNetActions = require('../../shared/actions/SocialNetActions');
var ChatActions = require('../../shared/actions/ChatActions');
var NavStore = require('../../shared/stores/NavStore');
var AuthStore = require('../../shared/stores/AuthStore');
var Utils = require('../../shared/common/Utils');

/**
 * Расписано как надо юзать реакт компоненты через browserify:
 * https://gist.github.com/sebmarkbage/d7bce729f38730399d28
 * https://github.com/facebook/react/issues/2436
 */
var App = React.createClass({
    componentWillMount: function () {
        //restore username & token from cookies before component mounted
        AuthActions.restoreUsernameAndTokenFromCookies();
        if (!AuthStore.hasCredentials()) {
            AuthActions.initFbAuth();
//            alert('wtf? you\'re not logged in!');
//            Utils.navigateToStart();
        }
        SocialNetActions.restoreSearchParamsFromCookies();
    },
    componentDidMount: function () {
        NavStore.addChangeListener(this._navStateChange);
        ChatActions.subscribe();
    },

    componentWillUnmount: function () {
        NavStore.removeChangeListener(this._navStateChange);
        ChatActions.unSubscribe();
    },

    _navStateChange: function () {
        //NB! This dummy function should be here because in othercase callback for nav store won't be initialized in proper order.
    },

    render: function () {
        return (
            <RouteHandler/>
        )
    }
});

var route = (
    <Route path="/" handler={App}>
        <Route name="user" path="user" handler={require('./left-pane/settings/user.js')}/>
        <Route name="landlord" path="user/landlord" handler={require('./left-pane/settings/landlord.js')}/>
        <Route name="renter" path="user/renter" handler={require('./left-pane/settings/renter.js')}/>

        <Route name="support" path="support" handler={require('./main/support.js')}/>
        <Route name="chats" path="user/chats" handler={require('./left-pane/chats/main')}/>
        <Route name="chat" path="user/chat" handler={require('./left-pane/chats/chat')}/>
        <Route name="newMessageInChat" path="user/chats/newMessage" handler={require('./left-pane/chats/newMessage')}/>
        <Route name="advert" path="advert/:id" handler={require('./left-pane/socialnet/advert')}  />

        <DefaultRoute handler={require('./left-pane/socialnet/social_networks')}/>
    </Route>
);

Router.run(route, function (Handler) {
    React.render(<Handler/>, document.getElementById('mainView'));
});