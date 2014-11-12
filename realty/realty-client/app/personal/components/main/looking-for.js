/** @jsx React.DOM */

var LookingForComponent = React.createClass({
    render: function () {
        return (
            <div className="panel">

                <div className="panel-body">
                    <h4>Мне интересно</h4>

                    <div className="col-md-6">
                        <form className="form-horizontal" role="form">

                            <div className="form-group">
                                <label className="col-md-2 control-label">Район</label>
                                <div className="col-md-8">
                                    <input className="form-control" placeholder="Район" type="text"/>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
});
React.render(
    <LookingForComponent />,
    document.getElementById('lookingForComponent')
);