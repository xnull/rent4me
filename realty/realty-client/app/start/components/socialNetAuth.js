/**
 * Created by null on 02.01.15.
 */
var React = require('react');
var AuthActions = require('../../shared/actions/AuthActions');
var UserActions = require('../../shared/actions/UserActions');
var Utils = require('rent4meUtil');
var assign = require('object-assign');

var Modal = require('react-bootstrap/Modal');
var ModalTrigger = require('react-bootstrap/ModalTrigger');
var Button = require('react-bootstrap/Button');


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

                    <div className='col-md-12'>
                        <div className="row">
                            <div className="col-md-12" >
                                <a href="javascript:void(0)" onClick={this.handleFacebookLogin}>
                                    <img width="192" src="images/signin/fb-long3.png" border="0"/>
                                </a>
                            </div>

                            <div className="col-md-12" onClick={this.handleVkLogin}>
                                <a href="javascript:void(0)">
                                    <img width="192" src="images/signin/vk-long.png" border="0"/>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

var PlainRegistration = React.createClass({
    propTypes: {
        shown: React.PropTypes.bool
    },


    getInitialState: function () {
        return {
            name: null,
            email: null,
            password: null,
            phone: null
        }
    },

    onEmailChange: function (e) {
        var value = e.target.value;

        console.log('new email:' + value);

        this.setState(assign(this.state, {
            email: value
        }));
    },

    onNameChange: function (e) {
        var value = e.target.value;

        console.log('new name:' + value);

        this.setState(assign(this.state, {
            name: value
        }));
    },

    onPasswordChange: function (e) {
        var value = e.target.value;

        console.log('new password:' + value);

        this.setState(assign(this.state, {
            password: value
        }));
    },

    onPhoneChange: function (e) {
        var value = e.target.value;

        console.log('new phone:' + value);

        this.setState(assign(this.state, {
            phone: value
        }));
    },

    onRegistrationClicked: function () {
        console.log('Reg clicked.');

        var name = this.state.name;
        var email = this.state.email;
        var password = this.state.password;
        var phone = this.state.phone;

        UserActions.registerOnBackendWithEmailAndPassword(name, email, password, phone);
    },

    render: function () {
        var inputStyle = {marginTop: '10px', padding: '0 0 0 0.85em'};

        var shown = this.props.shown || false;

        console.log('Registration shown? ' + shown);

        var name = this.state.name;
        var email = this.state.email;
        var password = this.state.password;
        var phone = this.state.phone;

        var style = {};

        if (!shown) {
            style['display'] = 'none';
        }

        var registrationButtonStyle = {marginTop: '10px'};

        return (
            <div className="row row-centered" style={style}>
                <div className='col-centered'>
                    Регистрация
                </div>

                <div className='col-md-12'>
                    <form role="form" className="form-horizontal">
                        <div className="row">
                            <div className="col-md-12">

                                <div>
                                    <input type="text"
                                        style={inputStyle}
                                        className="form-control"
                                        value={name}
                                        placeholder="Имя"
                                        onChange={this.onNameChange}
                                    />
                                </div>
                            </div>
                            <br/>
                            <div className="col-md-12">
                                <div>
                                    <input type="email"
                                        style={inputStyle}
                                        value={email}
                                        name="email"
                                        className="form-control"
                                        placeholder="E-mail"
                                        onChange={this.onEmailChange}
                                    />
                                </div>
                            </div>
                            <br/>
                            <div className="col-md-12">
                                <div>
                                    <input type="password"
                                        style={inputStyle}
                                        className="form-control"
                                        value={password}
                                        placeholder="Пароль"
                                        onChange={this.onPasswordChange}
                                    />
                                </div>
                            </div>
                            <br/>
                            <div className="col-md-12">
                                <div>
                                    <input type="text"
                                        style={inputStyle}
                                        value={phone}
                                        name="name"
                                        className="form-control"
                                        placeholder="Номер телефона (+7 915 ххх хх хх)"
                                        onChange={this.onPhoneChange}
                                    />
                                </div>
                            </div>
                            <br/>
                            <div className="col-md-12">
                                <a type="button"
                                    className="btn btn-success pull-right clickable"
                                    style={registrationButtonStyle}
                                    onClick={this.onRegistrationClicked}
                                >Зарегистрироваться</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
});

var PlainLogin = React.createClass({
    propTypes: {
        shown: React.PropTypes.bool
    },

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
        var inputStyle = {marginTop: '10px', padding: '0 0 0 0.85em'};

        var loginButtonStyle = {marginTop: '10px'};

        var email = this.state.email;
        var password = this.state.password;

        var shown = this.props.shown || false;

        console.log('Plain login shown? ' + shown);

        var style = {};

        if (!shown) {
            style['display'] = 'none';
        }

        return (
            <div className="row row-centered" style={style}>
                <div className='col-centered'>
                    Вход
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
                                <div>
                                    <a href="javascript:void(0)"
                                        className="btn btn-success pull-right"
                                        style={loginButtonStyle}
                                        onClick={this.onLoginClicked}>Вход</a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
});

var AuthModalDialog = React.createClass({
    getInitialState: function () {
        return {
            regShown: false
        }
    },

    render: function () {
        var regShown = this.state.regShown;
        var self = this;
        var switchLinkValue = !regShown ? "Зарегистрироваться" : "Войти";
        var targetFunc = regShown ?
            function (e) {
                console.log('Выключить авторизацию');
                self.setState(assign(self.state, {
                    regShown: false
                }));
            } :
            function (e) {
                self.setState(assign(self.state, {
                    regShown: true
                }));
            };

        return (
            <div className="modal container" id="myModal" tabIndex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
                <div className="modal-dialog" style={{width: '800px'}}>
                    <div className="modal-content" style={{color: '#000000'}}>
                        <div className="modal-header">
                            <span className="modal-title" id="myModalLabel">
                                Авторизация
                            </span>
                        </div>
                        <div className="modal-body">
                            <div className='row'>
                                <div className='col-md-6' style={{borderRight: '1px solid #333'}}>
                                    <PlainLogin shown={!regShown}/>
                                    <PlainRegistration shown={regShown}/>
                                    <br/>
                                    <a className="clickable btn btn-danger" onClick={targetFunc}>{switchLinkValue}</a>
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

var AuthModalDialog2 = React.createClass({
    getInitialState: function () {
        return {
            regShown: false
        }
    },

    onCloseClicked: function () {
        var closeFunc = this.props.onRequestHide;
        if (closeFunc) {
            closeFunc();
        }
    },

    render: function () {
        var regShown = this.state.regShown;
        var self = this;
        var switchLinkValue = !regShown ? "Зарегистрироваться" : "Войти";
        var targetFunc = regShown ?
            function (e) {
                console.log('Выключить авторизацию');
                self.setState(assign(self.state, {
                    regShown: false
                }));
            } :
            function (e) {
                self.setState(assign(self.state, {
                    regShown: true
                }));
            };

        var title = regShown ? "Регистрация" : "Авторизация";


        var content = regShown
            ?
            (
                <div className='row'>
                    <div className='col-md-12'>
                        <PlainRegistration shown={true}/>
                        <br/>
                        <a className="clickable btn btn-danger" onClick={targetFunc}>{switchLinkValue}</a>
                    </div>
                </div>
            )
            :
            (
                <div className='row'>
                    <div className='col-md-6' style={{borderRight: '1px solid #333'}}>
                        <PlainLogin shown={true}/>
                        <br/>
                        <a className="clickable btn btn-danger" onClick={targetFunc}>{switchLinkValue}</a>
                    </div>

                    <div className='col-md-6'>
                        <SocialNetAuth />
                    </div>
                </div>
            );


        return (
            <div>
                <Modal {...this.props} title={title} animation={false} closeButton={false}>
                    <div className="modal-body">
                        {content}
                    </div>
                    <div className="modal-footer">
                        <a href="javascript:void(0)" type="button" className="btn btn-default" onClick={this.onCloseClicked}>Закрыть</a>
                    </div>
                </Modal>
            </div>
        );
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


var AuthForm2 = React.createClass({
    render: function () {

        return (
            <div>
                <div>
                    <ModalTrigger modal={<AuthModalDialog2 />}>
                        <input type="button" className="button special" value="Вход" style={{
                            backgroundColor: '#dadada',
                            color: '#000000'
                        }} />
                    </ModalTrigger>
                </div>
            </div>
        );
    }
});


var AuthComponent = React.createClass({
    render: function () {
        //return <AuthForm/>
        return <AuthForm2/>
    }
});

module.exports = AuthComponent;
