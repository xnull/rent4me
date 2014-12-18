/**
 * Created by null on 18.12.14.
 */
var React = require('react');
var NavActions = require('../../../../shared/actions/NavActions');

var Renter = React.createClass({
    componentDidMount: function() {
        NavActions.navigateToRenter();
    },
    render: function () {
        return (
            <div className="col-md-9">
                <div className="panel">
                    <div className="panel-body">
                        <h4>В поисках жилья</h4>

                        <br/>

                    </div>
                </div>
            </div>
        );
    }
});

module.exports = Renter;