/**
 * Created by null on 02.01.15.
 */
var React = require('react');
var AuthActions = require('../../shared/actions/AuthActions');
var Utils = require('rent4meUtil');
var assign = require('object-assign');

var SocialNetAuth = React.createClass({
    handleFacebookLogin: function () {
        console.log('handle fb login');
        AuthActions.loginWithFB();
    },

    handleVkLogin: function () {
        console.log('handle vk login');
        AuthActions.loginWithVK();
    },

    render: function () {
        return (
            <div>
                <div className="row">
                    <div className='col-md-12'>
                        Социальные сети
                    </div>

                    <div className="row">
                        <div className="col-md-12" onClick={this.handleFacebookLogin}>
                            <img className="clickable" width="192" src="images/signin/fb-long3.png" />
                        </div>

                        <div className="col-md-12" onClick={this.handleVkLogin}>
                            <img className="clickable" width="192" src="images/signin/vk-long.png"/>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

var PlainLogin = React.createClass({

    getInitialState: function () {
        return {
            email: null,
            password: null
        }
    },

    onEmailChange: function (e) {
        var value = e.target.value;

        console.log('new email:' + value);

        this.setState(assign(this.state, {
            email: value
        }));
    },

    onPasswordChange: function (e) {
        var value = e.target.value;

        console.log('new password:' + value);

        this.setState(assign(this.state, {
            password: value
        }));
    },

    onLoginClicked: function () {
        console.log('Login clicked');
        AuthActions.loginOnBackendWithEmailAndPassword(this.state.email, this.state.password);
    },

    render: function () {
        var inputStyle = {border: '1px solid #c0c0c0', color: '#000000', textAlign: 'left', marginTop: '10px'};

        var loginButtonStyle = {marginTop: '10px'};

        var email = this.state.email;
        var password = this.state.password;


        return (
            <div className="row row-centered">
                <div className='col-centered'>
                    Аккаунт
                </div>

                <div className='col-md-12'>
                    <form role="form">
                        <div className="row">
                            <div className="col-md-12">
                                <input type="email"
                                    style={inputStyle}
                                    value={email}
                                    className="form-control"
                                    placeholder="E-mail"
                                    onChange={this.onEmailChange}
                                />
                            </div>
                            <br/>
                            <div className="col-md-12">
                                <input type="password"
                                    style={inputStyle}
                                    className="form-control"
                                    value={password}
                                    placeholder="Пароль"
                                    onChange={this.onPasswordChange}
                                />
                            </div>
                            <br/>
                            <div className="col-md-12">
                                <a type="button"
                                    className="btn btn-success pull-right clickable"
                                    style={loginButtonStyle}
                                    onClick={this.onLoginClicked}
                                >Вход</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
});

var AuthModalDialog = React.createClass({
    render: function () {
        return (
            <div className="modal container" id="myModal" tabIndex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content" style={{color: '#000000'}}>
                        <div className="modal-header">
                            <span className="modal-title" id="myModalLabel">
                                Авторизация
                            </span>
                        </div>
                        <div className="modal-body">
                            <div className='row'>
                                <div className='col-md-6' style={{borderRight: '1px solid #333'}}>
                                    <PlainLogin />
                                </div>

                                <div className='col-md-6'>
                                    <SocialNetAuth />
                                </div>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-default" data-dismiss="modal">Закрыть</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});


var AuthForm = React.createClass({
    render: function () {
        return (
            <div style={{backgroundColor: '#dadada', color: '#000000'}}>
                <input type="submit" className="button" value="Вход" data-toggle="modal" data-target="#myModal" style={{color: '#000000'}}/>
                <AuthModalDialog />
            </div>
        )
    }
});


var AuthComponent = React.createClass({
    render: function () {
        return <AuthForm/>
    }
});

module.exports = AuthComponent;
