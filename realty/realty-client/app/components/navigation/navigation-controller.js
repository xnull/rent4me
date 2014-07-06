var navigationController = function ($scope, navigationService, authorizationService, $log) {
    'use strict';

    $log.debug("Nav controller");

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

    this.isAuthorized = authorizationService.isAuthorized();
};

var navigationService = function () {
    "use strict";

    var home = 'home';
    var register = 'register';
    var login = 'login';
    var rent = 'rent';
    var rentSearch = 'rent-search';

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
        }
    };

};
