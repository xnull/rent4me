require('react');
var Router = require('react-router');

var Route = Router.Route;
var NotFoundRoute = Router.NotFoundRoute;
var DefaultRoute = Router.DefaultRoute;
var Link = Router.Link;
var RouteHandler = Router.RouteHandler;

/**
 * Расписано как надо юзать реакт компоненты через browserify:
 * https://gist.github.com/sebmarkbage/d7bce729f38730399d28
 * https://github.com/facebook/react/issues/2436
 */
var App = React.createClass({
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