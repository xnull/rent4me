/**
 * Created by null on 02.01.15.
 */
var React = require('react');
var AuthActions = require('../../shared/actions/AuthActions');
var Utils = require('rent4meUtil');

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

    render: function () {
        var inputStyle = {border: '1px solid #c0c0c0', color: '#000000', textAlign: 'left'};

        return (
            <div className="row row-centered" style={Utils.inactiveUi}>
                <div className='col-centered'>
                    Аккаунт
                </div>

                <div className='col-md-12'>
                    <form role="form">
                        <div className="row">
                            <div className="col-md-12">
                                <input type="email" style={inputStyle} className="form-control button" id="inputEmail" placeholder="E-mail"/>
                            </div>
                            <br/>
                            <div className="col-md-12">
                                <input type="password" style={inputStyle} className="form-control button" id="inputPassword" placeholder="Пароль"/>
                            </div>
                            <br/>
                            <div className="col-md-12">
                                <button type="submit" className="btn btn-success pull-right">Вход</button>
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
