var React = require('react');
var Router = require('react-router');

var Route = Router.Route;
var NotFoundRoute = Router.NotFoundRoute;
var DefaultRoute = Router.DefaultRoute;
var Link = Router.Link;
var RouteHandler = Router.RouteHandler;

var UserActions = require('../../shared/actions/UserActions');

var Auth = require('../../shared/common/Auth');
var Utils = require('../../shared/common/Utils');

/**
 * Расписано как надо юзать реакт компоненты через browserify:
 * https://gist.github.com/sebmarkbage/d7bce729f38730399d28
 * https://github.com/facebook/react/issues/2436
 */
var App = React.createClass({
    componentWillMount: function() {
        //restore username & token from cookies before component mounted
        Auth.restoreUsernameAndTokenFromCookies();
        if(!Auth.hasCredentials()) {
//            alert('wtf? you\'re not logged in!');
            Utils.navigateToStart();
        } else {
//            alert('Welcome back, '+Auth.username+"!");
            //TODO: redirect to personal page
        }
    },
    render: function () {
        return (
            <RouteHandler/>
        )
    }
});

var route = (
    <Route path="/" handler={App}>
        <Route name="user" path="user" handler={require('./left-pane/user.js')}/>
        <Route name="landlord" path="user/landlord" handler={require('./left-pane/settings/landlord.js')}/>
        <DefaultRoute handler={require('./main/news-pane.js')}/>
    </Route>
);

Router.run(route, function (Handler) {
    React.render(<Handler/>, document.getElementById('mainView'));
});