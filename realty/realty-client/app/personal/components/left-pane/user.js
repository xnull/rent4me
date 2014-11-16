/** @jsx React.DOM */

var UserComponent = React.createClass({
    render: function () {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <h4>Пользователь</h4>

                    <div className="well well-sm">
                        <div className="media">
                            <a className="thumbnail pull-left" href="#">
                                <img className="media-object" src="//placehold.it/80"/>
                            </a>

                            <div className="media-body">
                                <h4 className="media-heading">John Doe</h4>
                                <br/>

                                <p>
                                    <a href="#" className="btn btn-default">Настройки</a>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});
React.render(
    <UserComponent />,
    document.getElementById('userComponent')
);