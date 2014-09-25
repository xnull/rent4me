/**
 * Created by null on 06.07.14.
 */
var personalModule = (function () {
    'use strict';

    var cfg = {
        moduleName: 'rentApp.personal',
        moduleDependencies: ['ui.router'],

        ctlName: 'PersonalController',
        serviceName: 'personalService',
        stateName: 'personalState',
        stateConfig: {
            url: '/personal',
            templateUrl: 'personal/personal-view.html',
            controller: 'PersonalController'
        }
    };

    var angularModule = angular.module(cfg.moduleName, cfg.moduleDependencies);
    var angularLogger = angular.injector([cfg.moduleName, 'ng']).get('$log');

    function init() {
        angularLogger.debug('Loading "' + cfg.moduleName + '" module');

        angularModule.config(function ($stateProvider) {
            $stateProvider.state(cfg.stateName, cfg.stateConfig);
        });

        angularModule.controller(cfg.ctlName, controller);
        angularModule.factory(cfg.serviceName, service);
    }

    function controller($log, $scope, personalService, navigationService) {
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
    }

    function service() {
    }

    return {
        init: init,
        ctl: controller,
        srv: service
    };
})();

personalModule.init();