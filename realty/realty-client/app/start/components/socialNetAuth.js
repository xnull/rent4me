/**
 * Created by null on 02.01.15.
 */
var React = require('react');
var AuthActions = require('../../shared/actions/AuthActions');
var UserActions = require('../../shared/actions/UserActions');
var UserStore = require('../../shared/stores/UserStore');
var Utils = require('rent4meUtil');
var assign = require('object-assign');

var Modal = require('react-bootstrap/Modal');
var ModalTrigger = require('react-bootstrap/ModalTrigger');
var Button = require('react-bootstrap/Button');

var Validator = require('validator.js');
var _ = require('underscore');


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
            passwordConfirmation: null,
            phone: null,
            lastInputChangeMs: 0,
            errors: [],
            displayValueToFieldNameMap: {
                'name': 'Имя',
                'email': 'E-mail',
                'password': 'Пароль',
                'passwordConfirmation': 'Подтверждение пароля'
            }
        }
    },

    performIfInputWOntChange: function(callback){
        var now = (new Date()).getTime();
        var self = this;

        this.setState(assign(this.state, {
            lastInputChangeMs: now
        }));

        setTimeout(function () {
            console.log('timed out');
            console.log('states value: ' + self.state.lastInputChangeMs);
            console.log('remembered value: ' + now);
            if (self.state.lastInputChangeMs == now) {
                callback();
            }
        }, 500);
    },

    componentDidMount: function() {
        UserStore.addServerErrorListener(this.onServerError);
    },

    componentWillUnmount: function() {
        UserStore.removeServerErrorListener(this.onServerError);
    },

    onServerError: function() {
        var serverErrors = UserStore.getServerErrors();

        var errors = _.union((this.state.errors || []), serverErrors);//perform array concatenation

        this.setState(assign(this.state, {errors: errors}));
    },

    validateAll: function() {
        return this.validateForm(true);
    },

    validateForm: function (checkAll) {
        var data = this.state;

        var Assert = Validator.Assert;
        var Constraint = Validator.Constraint;
        var validator = new Validator.Validator();
        var constraintMap = {};

        constraintMap['name'] = [new Assert().NotBlank()];
        constraintMap['email'] = [new Assert().NotBlank(), new Assert().Email()];
        constraintMap['password'] = [new Assert().NotBlank(), new Assert().Length({min: 6})];
        constraintMap['passwordConfirmation'] = [new Assert().NotBlank(), new Assert().EqualTo(data['password'])];

        var defaultValuesMap = {
            'name': ['', null],
            'email': ['', null],
            'password': ['', null],
            'passwordConfirmation': ['', null]
        };

        var fieldToErrorMessageMap = {
            'name': 'Имя необходимо',
            'email': 'E-mail обязателен',
            'password': 'Пароль должен быть не короче 6 символов',
            'passwordConfirmation': 'Подтвержденный пароль не соответствует введенному'
        };


        var mergedMap = data;
        if(!checkAll) {
            var blackListedKeys = _.filter(_.keys(data),
                function (key) {
                    var hasNoProperty = !_.has(constraintMap, key);
                    if(hasNoProperty) {
                        console.log('Property will be blacklisted because it\'s not in constraint map: '+key);
                        return true;
                    }

                    var equalsToDefault = _.contains(defaultValuesMap[key], mergedMap[key]);
                    if(equalsToDefault) {
                        console.log('Property will be blacklisted because it\'s equal to default: '+key);
                        console.log('Data: '+mergedMap[key]);
                        console.log('Default value: '+defaultValuesMap[key]);
                        return true;
                    } else {
                        console.log('Property differs from default: '+key);
                        console.log('Data: '+mergedMap[key]);
                        console.log('Default value: '+defaultValuesMap[key]);
                    }

                    return false;
                });

            console.log('Keys that won\'t be checked: ');
            console.log(blackListedKeys);

            constraintMap = _.omit(constraintMap, blackListedKeys);

            console.log('Keys that will be checked: ');
            console.log(_.keys(constraintMap));
        }


        //filter map
        //constraintMap = _.(constraintMap).filter(function(constraint){
        //    return constraint.
        //});

        var constraint = new Constraint(constraintMap, {strict: true});

        var validationResult = validator.validate(mergedMap, constraint);

//        console.log(validationResult);

        console.log('Validation result:');
        console.log(validationResult);

        var self = this;

        var validationSucceeded = validationResult === true;

        var errors = _.map(validationResult, function (val, k) {
            return {key: self.state.displayValueToFieldNameMap[k], value: fieldToErrorMessageMap[k]};
        });

        this.setState(assign(this.state, {errors: errors}));

        return validationSucceeded;
    },

    onEmailChange: function (e) {
        var value = e.target.value;

        console.log('new email:' + value);

        this.setState(assign(this.state, {
            email: value
        }));


        this.performIfInputWOntChange(this.validateForm);

    },

    onNameChange: function (e) {
        var value = e.target.value;

        console.log('new name:' + value);

        this.setState(assign(this.state, {
            name: value
        }));

        this.performIfInputWOntChange(this.validateForm);
    },

    onPasswordChange: function (e) {
        var value = e.target.value;

        console.log('new password:' + value);

        this.setState(assign(this.state, {
            password: value
        }));

        this.performIfInputWOntChange(this.validateForm);
    },

    onPasswordConfirmationChange: function (e) {
        var value = e.target.value;

        console.log('new password:' + value);

        this.setState(assign(this.state, {
            passwordConfirmation: value
        }));

        this.performIfInputWOntChange(this.validateForm);
    },

    onPhoneChange: function (e) {
        var value = e.target.value;

        console.log('new phone:' + value);

        this.setState(assign(this.state, {
            phone: value
        }));

        this.performIfInputWOntChange(this.validateForm);
    },

    onRegistrationClicked: function () {
        if(!this.validateAll()) return;
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
        var passwordConfirmation = this.state.passwordConfirmation;
        var phone = this.state.phone;

        var errors = this.state.errors || [];

        var errorsHtml = errors.map(function(e){
            return <li key={e.key}>{e.key}: {e.value}</li>
        });

        var errorsHtmlOrNull = _.size(errors) > 0 ?
            (
                <div className='col-md-12'>
                    <div className='alert alert-danger' role='alert' >
                        <ul>
                        {errorsHtml}
                        </ul>
                    </div>
                </div>
            )
            : null;

        var style = {};

        if (!shown) {
            style['display'] = 'none';
        }

        var registrationButtonStyle = {marginTop: '10px'};

        return (
            <div className="row row-centered" style={style}>

                {errorsHtmlOrNull}

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
                                    <input type="password"
                                        style={inputStyle}
                                        className="form-control"
                                        value={passwordConfirmation}
                                        placeholder="Подтвердите пароль"
                                        onChange={this.onPasswordConfirmationChange}
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
        return <AuthForm2/>
    }
});

module.exports = AuthComponent;
