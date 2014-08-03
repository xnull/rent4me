/**
 * Created by null on 04.07.14.
 */
'use strict';

/* https://github.com/angular/protractor/blob/master/docs/getting-started.md */

describe('test of navigation', function () {

    browser.get('index.html');

    it('Check main page', function () {
        expect(browser.getLocationAbsUrl()).toMatch("http://rent4.me/dev/index.html");
    });



});