var configurator = require('../core/configurator.js');
var validator = require('../core/validator.js');

function NavigationController($scope, navigationService, AuthService, $log) {
    'use strict';
    $log.debug("Nav controller");

    validator().checkUndefinedBatch([$scope, navigationService, AuthService, $log]);

    this.getTab = navigationService.getTab;
    this.isSelected = navigationService.isSelected;
    this.selectTab = navigationService.selectTab;

    this.isHome = navigationService.isHome;
    this.setHome = navigationService.setHome;

    this.isLogin = navigationService.isLogin;
    this.setLogin = navigationService.setLogin;

    this.isRegister = navigationService.isRegister;
    this.setRegister = navigationService.setRegister;

    this.isRent = navigationService.isRent;
    this.setRent = navigationService.setRent;

    this.isRentSearch = navigationService.isRentSearch;
    this.setRentSearch = navigationService.setRentSearch;

    this.isRenterSearch = navigationService.isRenterSearch;
    this.setRenterSearch = navigationService.setRenterSearch;

    this.isPersonal = navigationService.isPersonal;
    this.setPersonal = navigationService.setPersonal;

    this.isAuthorized = AuthService.isAuthorized();
}

function navigationService() {
    "use strict";

    var home = 'home';
    var register = 'register';
    var login = 'login';
    var rent = 'rent';
    var rentSearch = 'rent-search';
    var renterSearch = 'renter-search';
    var personal = 'personal';

    this.tab = home;//set default value
    var _self = this;

    return {
        getTab: function () {
            return _self.tab;
        },
        selectTab: function (setTab) {
            _self.tab = setTab;
        },
        isSelected: function (checkTab) {
            return this.getTab() === checkTab;
        },

        isHome: function () {
            return this.isSelected(home);
        },
        setHome: function () {
            this.selectTab(home);
        },

        isRegister: function () {
            return this.isSelected(register);
        },
        setRegister: function () {
            this.selectTab(register);
        },

        isLogin: function () {
            return this.isSelected(login);
        },
        setLogin: function () {
            this.selectTab(login);
        },

        isRent: function () {
            return this.isSelected(rent);
        },
        setRent: function () {
            this.selectTab(rent);
        },

        isRentSearch: function () {
            return this.isSelected(rentSearch);
        },
        setRentSearch: function () {
            this.selectTab(rentSearch);
        },

        isRenterSearch: function () {
            return this.isSelected(renterSearch);
        },
        setRenterSearch: function () {
            this.selectTab(renterSearch);
        },

        isPersonal: function () {
            return this.isSelected(personal);
        },
        setPersonal: function () {
            this.selectTab(personal);
        }
    };
}

var cfg = {
    moduleName: 'rentApp.navigation',
    moduleDependencies: [],

    ctlName: 'NavigationController',
    serviceName: 'navigationService',

    service: navigationService,
    controller: NavigationController
};

configurator().configureWithoutUi(cfg);