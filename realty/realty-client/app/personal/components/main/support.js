/**
 * Created by null on 18.12.14.
 */
var React = require('react');
var NavActions = require('../../../shared/actions/NavActions');

var Renter = React.createClass({
    componentDidMount: function () {
        NavActions.navigateToSupport();
    },

    render: function () {
        return (
            <div className="well">
                <form className="form-horizontal" role="form">
                    <h4>Задать вопрос</h4>
                    <div className="form-group" style={{padding: 14}}>
                        <textarea className="form-control" placeholder="Ваш вопрос" rows="3"></textarea>
                    </div>
                    <button className="btn btn-success pull-right" type="button">Отправить</button>
                </form>
                <br/>
                <br/>
            </div>
        );
    }
});

module.exports = Renter;