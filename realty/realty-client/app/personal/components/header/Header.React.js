/**
 * Header
 * Created by dionis on 08/12/14.
 */
var React = require('react');

var AuthActions = require('../../../shared/actions/AuthActions');
var AuthStore = require('../../../shared/stores/AuthStore');
var NavStore = require('../../../shared/stores/NavStore');
var NavActions = require('../../../shared/actions/NavActions');
var AuthComponent = require('../../../shared/components/socialNetAuth');

var assign = require('object-assign');
var Utils = require('rent4meUtil');

var Header = React.createClass({
    getInitialState: function () {
        return {page: NavStore.getCurrentPage()};
    },

    componentDidMount: function () {
        console.log('Nav header will mount');
        NavStore.addChangeListener(this._navStateChangeListener);
        console.log('Header mounted');
        //this._navStateChangeListener();
    },

    componentWillUnmount: function () {
        NavStore.removeChangeListener(this._navStateChangeListener);
    },

    _navStateChangeListener: function () {
        console.log('Nav changed from:');
        console.log(this.state.page);
        console.log("To:");
        var currentPage = NavStore.getCurrentPage();
        console.log(currentPage);

        var newState = assign(this.state, {page: currentPage});

        console.log('New state:');
        console.log(newState);

        this.setState(newState);//trigger update
    },

    hiddenIfNotAuthorized: function () {
        if (Utils.isLocalhost()){
            return {};
        }
        return  AuthStore.hasCredentials() ? {} : {display: 'none'};
    },

    render: function () {

        var authorized = AuthStore.hasCredentials();

        var logoutOrLoginButton;
        if (authorized) {
            logoutOrLoginButton = <a href="javascript:void(0)" onClick={AuthActions.logoutOnBackend}>Выход</a>;
        } else {
            var authorizationDisplayItem = (<a href="javascript:void(0)">Вход / Регистрация</a>);
            logoutOrLoginButton = <AuthComponent displayItem={authorizationDisplayItem}/>;
        }

        var hiddenIfNotAuthorized = this.hiddenIfNotAuthorized();

        return (
            <div className='navbar navbar-default header' style={{borderRadius: 0}}>
                <div className='navbar-header'>
                    <img className='navbar-brand' style={{maxWidth: '100%'}} src="images/rent4me-icon.png"/>

                    <button type="button" className="navbar-toggle" data-toggle="collapse"
                            data-target="#navbar-collapse2">
                        <span className="sr-only">Toggle navigation</span>
                        <span className="icon-bar"></span>
                        <span className="icon-bar"></span>
                        <span className="icon-bar"></span>
                    </button>
                </div>
                <div className='collapse navbar-collapse' id='navbar-collapse2'>
                    <ul className='nav navbar-nav navbar-right'>
                        <li className={NavStore.isHomeSelected() ? 'active' : ''}>
                            <a href='#' role='button'>Поиск</a>
                        </li>

                        <li className={(NavStore.isLandLordSelected() || NavStore.isRenterSelected()) ? 'active dropdown' : 'dropdown'}
                            style={hiddenIfNotAuthorized}>
                            <a href='javascript:void(0)' className='dropdown-toggle' data-toggle='dropdown'>Мои
                                объявления
                                <b className='caret'></b>
                            </a>
                            <ul className='dropdown-menu pull-right'>
                                <li className={NavStore.isLandLordSelected() ? 'active' : ''}>
                                    <a href='#/user/landlord' role='button'>Я собственник</a>
                                </li>

                                <li className={NavStore.isRenterSelected() ? 'active' : ''} style={Utils.inactiveUi}>
                                    <a href='#/user/renter' role='button'>Я арендатор</a>
                                </li>
                            </ul>
                        </li>

                        <li style={hiddenIfNotAuthorized} className={NavStore.isCabinetSelected() ? 'active' : ''}>
                            <a href='#/user/cabinet' role='button'>Личный кабинет</a>
                        </li>

                        <li style={hiddenIfNotAuthorized} className={NavStore.isChatSelected() ? 'active' : ''}>
                            <a href='#/user/chats' role='button'>Сообщения</a>
                        </li>

                        <li className={NavStore.isSupportSelected() ? 'active' : ''}>
                            <a href='#/support' role='button'>Поддержка</a>
                        </li>

                        <li>
                            <a href="https://play.google.com/store/apps/details?id=rent4.me.rent" target="_blank">
                                Мобильная версия
                            </a>
                        </li>

                        <li>
                            {logoutOrLoginButton}
                        </li>
                    </ul>
                </div>

            </div>
        )
    }
});

module.exports = Header;