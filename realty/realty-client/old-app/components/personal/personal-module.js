/**
 * Created by null on 06.07.14.
 */
var configurator = require('../core/configurator.js');
var validator = require('../core/validator.js');

function controller($log, $scope, personalService, navigationService) {
    validator.checkUndefinedBatch([$log, $scope, personalService, navigationService]);

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

var cfg = {
    moduleName: 'rentApp.personal',
    moduleDependencies: ['ui.router', 'personal/personal-view.html'],

    ctlName: 'PersonalController',
    serviceName: 'personalService',
    stateName: 'personalState',
    stateConfig: {
        url: '/personal',
        templateUrl: 'personal/personal-view.html',
        controller: 'PersonalController'
    },
    controller: controller,
    service: service
};

configurator().configure(cfg);