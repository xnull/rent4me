/**
 * Created by null on 02.08.14.
 */
describe('Rent search test', function () {
    var _httpBackend;
    var _rentService;

    beforeEach(function () {

        // load the module.
        module(rentApplication.applicationName);

        // inject your service for testing.
        // The _underscores_ are a convenience thing
        // so you can have your variable name be the
        // same as your injected service.
        inject(function (rentSearchService, $httpBackend) {
            _rentService = rentSearchService;
            _httpBackend = $httpBackend;
        });
    });

    it('check hello method', function () {
        expect(_rentService).toBeDefined();
        expect(_rentService.hello()).toBe("hey hello");
    });
});
