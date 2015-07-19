var React = require('react');
var utils = require('../../app/shared/common/Utils.js');

describe("An utils", function () {
    it("optional must works correct", function () {
        var optionalOne = utils.Optional(1);
        var optionalTwo = utils.Optional(null);

        var resultOne = null;
        var resultTwo = null;

        optionalOne.ifPresent(function () {
            resultOne = 10
        });

        optionalTwo.ifPresent(function () {
            resultTwo = 10
        });

        expect(resultOne).toBe(10);
        expect(resultTwo).toBe(null);

    });
});
