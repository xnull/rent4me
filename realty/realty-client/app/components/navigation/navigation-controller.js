var navigationController = function ($scope, navigationService) {
    console.log("Nav controller");
//    $scope.navService = navigationService;
//    console.log("Tab: "+navigationService.tab);
//    console.log("is selected: "+navigationService.isSelected('home'));
//    navigationService.isSelected('home');
//    navigationService.selectTab('register');
//    navigationService.isSelected('home');
//    console.log("is selected: "+navigationService.isSelected('home'));
//    this.tab = navigationService.tab;
    this.selectTab = navigationService.selectTab;
    this.isSelected = navigationService.isSelected;
};

var navigationService = function () {
    "use strict";
    this.tab = 'home';
    var _self = this;

    console.log("Navigation service created");

    return {
        getTab: function() {
            return _self.tab;
        },
        selectTab: function (setTab) {
            _self.tab = setTab;
        },
        isSelected: function (checkTab) {
            return _self.tab === checkTab;
        }
    };

};
