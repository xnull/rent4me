/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var SocialNetActions = require('./SocialNetActions');
var MetrosActions = require('./MetrosActions');
var CityConstants = require('../constants/city-constants');
var CityStore = require('../stores/city-store');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');
var JSON = require('JSON2');
var Ajax = require('../common/Ajax');

var _cityRetrievalInProgress = false;

var Actions = {
    retrieveUserCity: function (lng, lat) {
        if(!_cityRetrievalInProgress) {
            _cityRetrievalInProgress = true;

            BlockUI.blockUI();

            var geoUrl = null;
            if (lat && lng) {

                geoUrl = 'lng=' + lng + '&lat=' + lat;
            }

            var url = '/rest/cities/search'+(geoUrl != null ? ('?'+geoUrl) : '');

            Ajax
                .GET(url)
                .onSuccess(function (city) {
                    AppDispatcher.handleViewAction({
                        actionType: CityConstants.CITY_CHANGED,
                        city: city
                    });

                    var bounds = {
                        northEast: {
                            lat: city.area.high.latitude,
                            lng: city.area.high.longitude
                        },

                        southWest: {
                            lat: city.area.low.latitude,
                            lng: city.area.low.longitude
                        }
                    };

                    SocialNetActions.changeSearchLocationInfo(city.area.center_point, null, bounds, city.name, city.name);

                    MetrosActions.findMetros(city.area.center_point.longitude, city.area.center_point.latitude,
                        null,
                        bounds);

                    BlockUI.unblockUI();
                })
                .onError(function (xhr, status, err) {
                    AppDispatcher.handleViewAction({
                        actionType: CityConstants.CITY_CHANGED,
                        city: []
                    });
                    BlockUI.unblockUI();
                })
                .execute();
        }
    }
};

module.exports = Actions;
