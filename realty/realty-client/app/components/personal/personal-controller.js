/**
 * Created by null on 06.07.14.
 */
var personalController = function ($log, $scope, personalService, navigationService) {
    "use strict";

    $scope.menuTabs = [
        {id: 'home', name: 'Home'},
        {id: 'settings', name: 'Settings'},
        {id: 'message', name: 'Messages'}
    ];

    $scope.currentMenuTabId = $scope.menuTabs[0].id;

    $scope.isActiveMenuTab = function (menuTabId) {
        $log.debug('Is menu active: ' + menuTabId);

        if ($scope.currentMenuTabId === menuTabId) {
            return 'active';
        }
        return '';
    };

    $scope.showMenu = function (menuTabId) {
        $log.info('Show subview: ' + menuTabId);
        $scope.currentMenuTabId = menuTabId;
    };

    $(function () {
        navigationService.setPersonal();
    });
};

var personalService = function () {
    "use strict";
};