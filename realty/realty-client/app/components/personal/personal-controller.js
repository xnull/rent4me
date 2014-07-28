/**
 * Created by null on 06.07.14.
 */

var personalModule = angular.module('rentApp.personal', []);

personalModule.controller('PersonalController', function ($log, $scope, personalService, navigationService) {
    "use strict";

    $scope.menuTabs = [
        {id: 'myAds', name: 'Мои объявления'},
        {id: 'addAds', name: 'Создать объявление'},
        {id: 'myRealty', name: 'Моя недвижимость'},
        {id: 'account', name: 'Аккаунт'},
        {id: 'messages', name: 'Мои сообщения'},
        {id: 'finances', name: 'Мои финансы'},
        {id: 'searches', name: 'Автопоиск'}
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
});

personalModule.factory('personalService', function () {
    "use strict";
});