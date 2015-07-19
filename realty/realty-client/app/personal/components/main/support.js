/**
 * Created by null on 18.12.14.
 */
var React = require('react');
var assign = require('object-assign');
var NavActions = require('../../../shared/actions/NavActions');
var AuthStore = require('../../../shared/stores/AuthStore');
var UserStore = require('../../../shared/stores/UserStore');
var FeedbackActions = require('../../../shared/actions/feedback-actions');

var Renter = React.createClass({
    getInitialState: function () {
        return {
            isAuthorized: AuthStore.hasCredentials(),
            email: '',
            name: '',
            text: '',
            infoMessage: ''
        }
    },

    componentDidMount: function () {
        NavActions.navigateToSupport();
        AuthStore.addChangeListener(this._onAuthChange);
    },

    componentWillUnmount: function () {
        AuthStore.removeChangeListener(this._onAuthChange);
    },

    _onAuthChange: function () {
        this.setState(assign({}, this.state, {
            isAuthorized: AuthStore.hasCredentials()
        }));
    },

    _onTextChange: function (e) {
        var value = e.target.value;
        this.setState(assign({}, this.state, {
            text: value
        }));
    },

    _onNameChange: function (e) {
        var value = e.target.value;
        this.setState(assign({}, this.state, {
            name: value
        }));
    },

    _onEmailChange: function (e) {
        var value = e.target.value;
        this.setState(assign({}, this.state, {
            email: value
        }));
    },

    _onClick: function (e) {
        console.log('Name: ' + this.state.name);
        console.log('Email: ' + this.state.email);
        console.log('Text: ' + this.state.text);
        FeedbackActions.sendFeedback(this.state.name, this.state.email, this.state.text);
        this.setState(assign({}, this.state, {
            email: '',
            name: '',
            text: '',
            infoMessage: 'Благодарим вас за обратную связь'
        }));
    },

    render: function () {
        var show = !this.state.isAuthorized;

        var nameInput = (<div className="row" style={{padding: 14}}>
            <input type="text" placeholder="Имя (необязательно)" className="form-control" value={this.state.name}
                   onChange={this._onNameChange}/>
        </div>);

        var emailInput = (<div className="row" style={{padding: 14}}>
            <input type="text" placeholder="E-Mail (необязательно)" className="form-control" value={this.state.email}
                   onChange={this._onEmailChange}/>
        </div>);

        if (!show) {
            emailInput = null;
            nameInput = null;
        }

        var infoMessageDisplay = this.state.infoMessage ?
            <p className="alert alert-info">{this.state.infoMessage}</p> : null;

        if (infoMessageDisplay) {
            return (
                <div className="well">
                    {infoMessageDisplay}
                </div>
            );
        } else {
            return (
                <div>
                    <br/>

                    <div className="well">
                        <form className="form-horizontal" role="form">
                            <h4>Обратная связь</h4>

                            <div className="form-group" style={{padding: 14}}>
                                {nameInput}
                                {emailInput}
                                <div className="row" style={{padding: 14}}>
                                    <textarea className="form-control" placeholder="Ваш вопрос" rows="3"
                                              value={this.state.text} onChange={this._onTextChange}/>
                                </div>
                            </div>
                            <button className="btn btn-success pull-right" type="button" onClick={this._onClick}>
                                Отправить
                            </button>
                        </form>
                        <br/>
                        <br/>
                    </div>
                </div>
            );
        }
    }
});

module.exports = Renter;