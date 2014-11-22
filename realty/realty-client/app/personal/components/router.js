/** @jsx React.DOM */

var Router = ReactRouter;
var Route = ReactRouter.Route;
var NotFoundRoute = ReactRouter.NotFoundRoute;
var DefaultRoute = ReactRouter.DefaultRoute;
var Link = ReactRouter.Link;
var RouteHandler = ReactRouter.RouteHandler;

var App = React.createClass({
    render: function () {
        return (
            <RouteHandler/>
        )
    }
});

var routes = (
    <Route path="/" handler={App}>
        <Route name="user" path="user" handler={Settings}/>
        <Route name="landlord" path="user/landlord" handler={LandlordSettings}/>
        <DefaultRoute handler={NewsPaneComponent}/>
    </Route>

);

Router.run(routes, function (Handler) {
    React.render(<Handler/>, document.getElementById('mainView'));
});