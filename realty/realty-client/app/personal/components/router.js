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
        </Route>
    </Routes>
);

React.renderComponent(routes, document.getElementById('mainView'));