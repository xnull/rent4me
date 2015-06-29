/**
 * Created by null on 6/21/15.
 */
var _ = require('underscore');
var Utils = require('rent4meUtil');

module.exports = {
    noImage: '/personal/images/not-available.jpeg',
    unrecognizedMetro: "См. в описании",

    getFirstImageUrl: function (item) {

        if (!item) {
            return this.noImage;
        }

        if (item.data_source === 'INTERNAL') {
            if (!item.photos || _.isEmpty(item.photos)) return this.noImage;

            return _.first(item.photos).full_picture_url;
        }

        if (!item.external_images || _.isEmpty(item.external_images)) {
            return this.noImage;
        }

        return _.first(item.external_images).full_picture_url;
    },

    getAddress: function (advertData) {
        return Utils.formatPostItemAddress(advertData);
    },

    getMetroList: function (metros) {
        var hasMetros = _.size(metros) > 0;qqq

        if (!hasMetros) {
            return this.unrecognizedMetro;
        }

        var result = metros.map(function (metro) {
            return metro.station_name;
        });

        return result.join('; ');
    },

    getPhone: function (item) {
        var hasContacts = item.contacts;

        var phoneNumbers = hasContacts ? item.contacts.filter(contact=>contact.type === 'PHONE').map(contact=>contact.phone) : [];

        return phoneNumbers.map(phone => {
            return phone.national_formatted_number || phone.raw_number
        });
    }
};