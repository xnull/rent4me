/** @jsx React.DOM */

var Router = ReactRouter;
var Route = ReactRouter.Route;
var Routes = ReactRouter.Routes;

var App = React.createClass({
    render: function () {
        return (
            <this.props.activeRouteHandler/>
        )
    }
});

var routes = (
    <Routes>
        <Route name="app" path="/" handler={App}>
            <Route name="main" path="main" handler={NewsPaneComponent}/>
            <Route name="user" path="user" handler={Settings}/>
            <Route name="landlord" path="user/landlord" handler={LandlordSettings}/>
        </Route>
    </Routes>
);

React.render(routes, document.getElementById('mainView'));