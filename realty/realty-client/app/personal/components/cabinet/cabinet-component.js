/**
 * Personal cabinet
 * Created by null on 7/19/15.
 */
var React = require('react');
var Left = require('../left-pane/left-pane.js');

var Cabinet = React.createClass({

    render: function () {
        return (
            <div className="panel">
                <div className="row">
                    <div className="col-md-3 col-sm-12 col-xs-12">
                        <Left />
                    </div>
                    <div className="col-md-9 col-sm-12 col-xs-12">

                    </div>
                </div>
            </div>
        )
    }
});

module.exports = Cabinet;