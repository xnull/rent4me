/**
 * Created by dionis on 08/12/14.
 */
var React = require('react');

var AuthActions = require('../../../shared/actions/AuthActions');
var AuthStore = require('../../../shared/stores/AuthStore');
var NavStore = require('../../../shared/stores/NavStore');
var NavActions = require('../../../shared/actions/NavActions');

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

    render: function () {
        return (
            <div className="navbar navbar-default" >
                <div className="col-md-12">
                    <div className="collapse navbar-collapse" id="navbar-collapse2">
                        <ul className="nav navbar-nav navbar-right">
                            <li className={NavStore.isHomeSelected() ? "active" : ""}>
                                <a href="#" role="button">Главная</a>
                            </li>

                            <li className={NavStore.isLandLordSelected() ? "active" : ""}>
                                <a href="#/user/landlord" role="button">Я собственник</a>
                            </li>

                            <li className={NavStore.isRenterSelected() ? "active" : ""} style={Utils.inactiveUi}>
                                <a href="#/user/renter" role="button">Я арендатор</a>
                            </li>

                            <li className={NavStore.isSupportSelected() ? "active" : ""} style={Utils.inactiveUi}>
                                <a href="#/support" role="button">Поддержка</a>
                            </li>

                            <li>
                                <a href="javascript:void(0)" onClick={AuthActions.logoutOnBackend}>Выход</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = Header;