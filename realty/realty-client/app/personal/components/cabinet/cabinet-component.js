/**
 * Personal cabinet
 * Created by null on 7/19/15.
 */
var React = require('react');
var Left = require('../left-pane/left-pane.js');
var Settings = require('../left-pane/settings/user.js');

var NavActions = require('../../../shared/actions/NavActions');
var AuthStore = require('../../../shared/stores/AuthStore');

var Cabinet = React.createClass({

    componentDidMount: function () {
        NavActions.navigateToCabinet();
        AuthStore.addChangeListener(this._onAuthChange);
    },

    componentWillUnmount: function() {
        AuthStore.removeChangeListener(this._onAuthChange);
    },

    _onAuthChange: function() {
        this.setState(assign({}, this.state, {
            isAuthorized: AuthStore.hasCredentials()
        }));
    },

    render: function () {
        return (
            <div className="panel">
                <div className="row">
                    <div className="col-md-3 col-sm-12 col-xs-12">
                        <Left />
                    </div>
                    <div className="col-md-9 col-sm-12 col-xs-12">
                        <Settings />
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = Cabinet;