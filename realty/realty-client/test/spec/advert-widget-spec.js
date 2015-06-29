/**
 * Created by null on 28.06.14.
 */

var React = require('react');
var advertHelper = require('../../app/personal/components/left-pane/socialnet/advert-widget-helper.js');

describe("An advert helper", function () {
    it("must return first image url", function () {
        expect(advertHelper.noImage).toBe('/personal/images/not-available.jpeg');
        expect(advertHelper.getFirstImageUrl()).toBe(advertHelper.noImage);

        expect(advertHelper.getFirstImageUrl(testExternalAdvert)).toBe(advertHelper.noImage);
    });

    it("must check address data", function () {
        var advertData = {city: 'Moscow'};
        expect(advertHelper.getAddress(advertData)).toBe('Moscow');
    });

    it("getMetros must returns metro list", function () {
        var metroList = [{station_name: 'metro1'}, {station_name: 'metro2'}];
        expect(advertHelper.getMetroList(metroList)).toEqual('metro1; metro2');
    });

    it("getPhone must returns the phone number", function () {
        var phones = {contacts: [{type: 'PHONE', phone: {raw_number: '123'}}]};
        expect(advertHelper.getPhone(phones)).toEqual(['123']);
    });
});

var testExternalAdvert = {
    "id": 329020,
    "location": {"latitude": 55.86073099999999, "longitude": 37.43646},
    "address": {"country_code": "RU"},
    "description": "Сдам комнату на длительный срок.",
    "fee_period": "MONTHLY",
    "created": "2015-06-23T20:43:53Z",
    "updated": "2015-06-23T20:50:05Z",
    "published": true,
    "metros": [{
        "id": 107,
        "station_name": "Планерная",
        "location": {"latitude": 55.86073099999999, "longitude": 37.43646}
    }],
    "data_source": "VKONTAKTE",
    "external_images": [],
    "contacts": [],
    "external_link": "https://vk.com/wall-54123806_59884",
    "external_author_link": "https://vk.com/id144211352",
    "city": "Москва"
};


var internalAdvertData = [{
    "id": 1,
    "location": {"latitude": 55.752023, "longitude": 37.61749899999995},
    "address": {
        "formatted_address": "Москва, Россия, 103073",
        "city": "Москва",
        "country": "Россия",
        "country_code": "RU",
        "zip_code": "103073"
    },
    "description": "cool cool",
    "room_count": 1,
    "floor_number": 1,
    "floors_total": 1,
    "area": 100,
    "type_of_rent": "LONG_TERM",
    "rental_fee": 50000,
    "fee_period": "MONTHLY",
    "created": "2015-06-21T18:06:18Z",
    "updated": "2015-06-21T18:06:18Z",
    "published": true,
    "metros": [],
    "data_source": "INTERNAL",
    "owner": {
        "id": 1,
        "username": null,
        "name": "Вячеслав Пец",
        "first_name": "Вячеслав",
        "last_name": "Пец",
        "email": "xnulltank@ya.ru",
        "phone": null,
        "fb_id": null,
        "vk_id": "12573970",
        "password": null
    },
    "photos": []
}];