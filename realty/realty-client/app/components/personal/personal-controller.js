/**
 * Created by null on 06.07.14.
 */
var personalModuleCfg = {
    ctlName: 'PersonalController',
    serviceName: 'personalService',
    stateName: 'personalState',
    stateConfig: {
        url: '/personal',
        templateUrl: 'components/personal/personal-view.html',
        controller: 'PersonalController'
    }
};

var personalModule = angular.module('rentApp.personal', ['ui.router']);

personalModule.config(function ($stateProvider) {
    'use strict';
    $stateProvider.state(personalModuleCfg.stateName, personalModuleCfg.stateConfig);
});

personalModule.controller(personalModuleCfg.ctlName, function ($log, $scope, personalService, navigationService) {
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

personalModule.factory(personalModuleCfg.serviceName, function () {
    "use strict";
});