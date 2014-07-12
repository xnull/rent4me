/**
 * Created by null on 29.06.14.
 */
'use strict';

/**
 * http://www.benlesh.com/2013/06/angular-js-unit-testing-services.html
 *
 * mock http://stackoverflow.com/questions/14773269/injecting-a-mock-into-an-angularjs-service
 *
 * http mocks and example of tests in: angularjs book 108 page, and unit test for ngResource: 112 page
 */
describe('Rent service test', function () {
    var _httpBackend;
    var _rentService;

    beforeEach(function () {

        // load the module.
        module('project');

        // inject your service for testing.
        // The _underscores_ are a convenience thing
        // so you can have your variable name be the
        // same as your injected service.
        inject(function (rentService) {
            _rentService = rentService;
        });

        inject(function ($httpBackend) {
         _httpBackend = $httpBackend;
        });
    });

    it('check hello method', function () {
        expect(_rentService).toBeDefined();
        expect(_rentService.hello()).toBe("hey hello");
    });
});
