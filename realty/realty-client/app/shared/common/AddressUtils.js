/**
 * Created by dionis on 05/12/14.
 */

//TODO: move this address parsing related stuff in some kind of generic helper library
var getShortName = function (addressComponent) {
    return addressComponent['short_name'];
};

var getLongName = function (addressComponent) {
    return addressComponent['long_name'];
};

var getCity = function (addressComponents) {
    addressComponents = addressComponents || [];


    console.log(addressComponents);

    var district = null;
    var area = null;
    var country = null;

    var len = addressComponents.length;

    var i;
    for (i = 0; i < len; i++) {
        var addressComponent = addressComponents[i];

        var types = addressComponent['types'] || [];

        var typeLen = types.length;


        for (var j = 0; j < typeLen; j++) {
            var type = types[j].toUpperCase();

            if (type == 'LOCALITY') {
                return getLongName(addressComponent);
            }
            if (type == 'SUBLOCALITY') {
                district = getLongName(addressComponent);
            }
            if (type == 'ADMINISTRATIVE_AREA_LEVEL_1') {
                area = getLongName(addressComponent);
            }
            if (type == 'COUNTRY') {
                country = getLongName(addressComponent);
            }
        }
    }

    return (!area ? (!district ? country : district) : area);
};

/**
 *
 * @param addressComponents
 * @param type - Supported values are one of:
 * STREET_NUMBER,
 STREET_ADDRESS,
 ROUTE,
 ROOM,
 CITY,
 STATE,
 ZIP,
 COUNTRY,
 COUNTRY_LONG,
 DISTRICT
 * @returns {null}
 */
var getAddressComponentOfTypeOrNull = function (addressComponents, targetType) {
    targetType = targetType || '';
    addressComponents = addressComponents || [];

    var len = addressComponents.length;
    for (var i = 0; i < len; i++) {
        var addressComponent = addressComponents[i];

        var types = addressComponent['types'] || [];

        var typeLen = types.length;
        for (var j = 0; j < typeLen; j++) {
            var type = types[j].toUpperCase();
            switch (targetType) {
                case 'COUNTRY':
                {
                    if ("COUNTRY" == type) return getShortName(addressComponent);
                    break;
                }
                case 'COUNTRY_LONG':
                {
                    if ("COUNTRY" == type) return getLongName(addressComponent);
                    break;
                }
                case 'CITY':
                {
                    return getCity(result);
                }
                case 'DISTRICT':
                {
                    if ('SUBLOCALITY' == type) {
                        return getLongName(addressComponent);
                    }

                    break;
                }
                case 'STREET_ADDRESS':
                {
                    if ('STREET_ADDRESS' == type) {
                        return getLongName(addressComponent);
                    }

                    break;
                }
                case 'STREET_NUMBER':
                {
                    if ('STREET_NUMBER' == type) {
                        return getLongName(addressComponent);
                    }

                    break;
                }
                case 'ROUTE':
                {
                    if ('ROUTE' == type) {
                        return getLongName(addressComponent);
                    }

                    break;
                }
                case 'ROOM':
                {
                    if ('ROOM' == type) {
                        return getLongName(addressComponent);
                    }

                    break;
                }
                case 'ZIP':
                {
                    if ('POSTAL_CODE' == type) {
                        return getLongName(addressComponent);
                    }

                    break;
                }
                case 'STATE':
                {
                    if ('ADMINISTRATIVE_AREA_LEVEL_1' == type) return getShortName(addressComponent);
                    break;
                }
//                        default:
//                            throw new UnsupportedOperationException("Unsupported operation type" + targetType);
            }
        }
    }
    //return null if nothing found
    return null;

};

var buildAddress = function (addressComponents) {
    var addressBuilder = '';

    var streetAddress = getAddressComponentOfTypeOrNull(addressComponents, 'STREET_ADDRESS');
    var route = getAddressComponentOfTypeOrNull(addressComponents, 'ROUTE');
    var streetNumber = getAddressComponentOfTypeOrNull(addressComponents, 'STREET_NUMBER');
    var room = getAddressComponentOfTypeOrNull(addressComponents, 'ROOM');

    if (streetAddress) addressBuilder.append(streetAddress);
    if (route != null) {
        if (addressBuilder.length > 0) addressBuilder += " ";
        addressBuilder += (route);
    }
    if (streetNumber != null) {
        if (addressBuilder.length > 0) addressBuilder += (" ");
        addressBuilder += (streetNumber);
    }
    if (room != null) {
        if (addressBuilder.length > 0) addressBuilder += (" - ");
        addressBuilder += (room);
    }

    return addressBuilder;
};

var R4MEAddressUtil = {
    getShortName: getShortName,
    getLongName: getLongName,
    getCity: getCity,
    getAddressComponentOfTypeOrNull: getAddressComponentOfTypeOrNull,
    buildAddress: buildAddress
};

module.exports = R4MEAddressUtil;
