/**
 * Created by null on 28.06.14.
 */

var React = require('react');
var advertHelper = require('../../app/personal/components/left-pane/socialnet/advert-widget-helper.js');

describe("An advert helper", function () {
    it("must return first image url", function () {
        expect(advertHelper.noImage).toBe('/personal/images/not-available.jpeg');
        expect(advertHelper.getFirstImageUrl()).toBe(advertHelper.noImage);

        var item = {data_source: 'INTERNAL', photos: [{small_thumbnail_url: 'url'}]};
        expect(advertHelper.getFirstImageUrl(item)).toBe('url');

        item = {data_source: 'EXTERNAL', external_images: [{small_thumbnail_url: 'url'}]};
        expect(advertHelper.getFirstImageUrl(item)).toBe('url')
    });

    it("must check address data", function () {
        var advertData = {city: 'Moscow'};
        expect(advertHelper.getAddress(advertData)).toBe('Moscow');
    });

    it("getMEtros must returns metro list", function () {
        var metroList = [{station_name: 'metro1'}, {station_name: 'metro2'}];
        expect(advertHelper.getMetroList(metroList)).toEqual(['metro1', 'metro2']);
    });

    it("getPhone must returns the phone number", function () {
        var phones = {contacts: [{type: 'PHONE', phone: {raw_number: '123'}}]};
        expect(advertHelper.getPhone(phones)).toEqual(['123']);
    });
});