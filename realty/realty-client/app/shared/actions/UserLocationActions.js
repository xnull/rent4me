/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var UserLocationConstants = require('../constants/UserLocationConstants');
var UserLocationStore = require('../stores/user-location-store');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');
var JSON = require('JSON2');
var Ajax = require('../common/Ajax');

var _locationRetrievalInProgress = false;

var Actions = {
    retrieveUserLocation: function () {
        if(UserLocationStore.getLocation() == null && !_locationRetrievalInProgress) {
            _locationRetrievalInProgress = true;

            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(position => {
                    if(position && position.coords && position.coords.longitude && position.coords.latitude) {
                        AppDispatcher.handleViewAction({
                            actionType: UserLocationConstants.LOCATION_CHANGED,
                            location: {
                                lat: position.coords.latitude,
                                lng: position.coords.longitude
                            }
                        });
                    }


                    _locationRetrievalInProgress = false;
                });
            } else {
                _locationRetrievalInProgress = false;

                AppDispatcher.handleViewAction({
                    actionType: UserLocationConstants.LOCATION_CHANGED,
                    location: null
                });
            }

        }
    }
};

module.exports = Actions;
