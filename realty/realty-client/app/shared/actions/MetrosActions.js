/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var MetroConstants = require('../constants/MetroConstants');
var MetroStore = require('../stores/MetrosStore');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');
var JSON = require('JSON2');
var Ajax = require('../common/Ajax');

var ApartmentActions = {
    findMetros: function (lng, lat, countryCode, bounds) {
        BlockUI.blockUI();

        var geoUrl = null;
        if (lat && lng) {

            geoUrl = 'lng=' + lng + '&lat=' + lat;
            if(countryCode) {
                geoUrl +=  "&country_code=" + countryCode;
            }
            if (bounds && bounds.northEast && bounds.southWest) {
                var ne = bounds.northEast;
                var sw = bounds.southWest;

                var urlValue = "lat_lo=" + sw.lat + "&lng_lo=" + sw.lng + "&lat_hi=" + ne.lat + "&lng_hi=" + ne.lng;
                geoUrl = geoUrl + "&" + urlValue;
                console.log("Bounds urlValue: " + urlValue);
            } else {
                console.log("Bounds no urlValue ");
            }
        }

        var url = '/rest/metros/search'+(geoUrl != null ? ('?'+geoUrl) : '');

        Ajax
            .GET(url)
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: MetroConstants.METROS_FOUND,
                    metros: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                AppDispatcher.handleViewAction({
                    actionType: MetroConstants.METROS_FOUND,
                    metros: []
                });
                BlockUI.unblockUI();
            })
            .execute();
    }
};

module.exports = ApartmentActions;
