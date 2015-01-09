/**
 * Created by null on 04.07.14.
 */
'use strict';

/* https://github.com/angular/protractor/blob/master/docs/getting-started.md */

function checkUrls(urls) {
    for (var i = 0; i < urls.length; ++i) {
        var url = urls[i].url;
        var absUrl = urls[i].absLocation;

        browser.get(url);

        it('Check page; ' + urls[i].url, function () {
            expect(browser.getLocationAbsUrl()).toMatch(absUrl);
        });
    }
}

describe('test of navigation', function () {

    var urls = [
        {
            url: 'index.html',
            absLocation: 'http://rent4.me/dev/index.html#/'
        },
        {
            url: '#/login',
            absLocation: 'http://rent4.me/dev/#/login'
        },
        {
            url: '#/register',
            absLocation: 'http://rent4.me/dev/#/register'
        },
        {
            url: '#/rent-search',
            absLocation: 'http://rent4.me/dev/#/rent-search'
        },
        {
            url: '#/renter-search',
            absLocation: 'http://rent4.me/dev/#/renter-search'
        },
        {
            url: '#/personal',
            absLocation: 'http://rent4.me/dev/#/personal'
        },
        {
            url: '#/rent',
            absLocation: 'http://rent4.me/dev/#/rent'
        },
        {
            url: '#/apartment-info/123',
            absLocation: 'http://rent4.me/dev/#/apartment-info/123'
        }
    ];

    checkUrls(urls);

    describe('Should contains a carousel', function () {

        beforeEach(function () {
            browser.get('index.html');
        });


        it('should render main page when user navigates to /', function () {
            expect(element.all(by.repeater('imgData in carouselData')).count()).toEqual(3);
            //console.log(element(by.model('carouselData')));
        });

    });

});