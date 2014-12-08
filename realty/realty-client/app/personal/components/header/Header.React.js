/**
 * Created by dionis on 08/12/14.
 */
var React = require('react');

var Auth = require('../../../shared/common/Auth');

var Header = React.createClass({
    render: function () {
        return (
            <div className="navbar navbar-default">
                <div className="col-md-12">
                    <div className="collapse navbar-collapse" id="navbar-collapse2">
                        <ul className="nav navbar-nav navbar-right">
                            <li className="active"><a href="#" role="button">Home</a></li>
                            <li><a href="#/user/landlord" role="button">Я собственник</a></li>
                            <li><a href="#aboutModal" role="button">Я арендатор</a></li>
                            <li><a href="javascript:none;" onClick={Auth.logoutOnBackend}>Выход</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            )
    }
});

module.exports = Header;