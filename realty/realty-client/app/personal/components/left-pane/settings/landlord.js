/**
 * Настройки собственности юзера
 */
require('react');
var Auth = require('../../common/auth.js');

var UserProperty = React.createClass({
    render: function () {
        var customClassName = this.props.data.customClassName || 'col-md-4';

        return (
            <div>
                <div className="form-group">
                    <label className="col-md-2 control-label">{this.props.data.name}:</label>
                    <div className={customClassName}>
                        <input
                            id={this.props.data.id}
                            className="form-control"
                            type="text"
                            placeholder={this.props.data.placeholder}
                            value={this.props.data.elementValue}
                        />
                    </div>
                </div>
            </div>
        )
    }
});

var UserSelect = React.createClass({
    render: function () {
        var customClassName = this.props.data.customClassName || 'col-md-4';

        var selectedValue = this.props.data.selectedValue;

        var keyValuePairs = this.props.data.keyValuePairs || [];
        var description = this.props.data.defaultDescription || 'Выберите из списка';
        keyValuePairs.unshift(['', description]);

        var optionNodes = keyValuePairs.map(function (pair) {
            return (
                <option key={pair[0]} value={pair[0]}>{pair[1]}</option>
            );
        });

        return (
            <div>
                <div className="form-group">
                    <label className="col-md-2 control-label">{this.props.data.name}:</label>
                    <div className={customClassName}>
                        <select id={this.props.data.id} className="form-control" value={selectedValue}>
                            {optionNodes}
                        </select>
                    </div>
                </div>
            </div>
        )
    }
});

var UserText = React.createClass({
    render: function () {
        var customClassName = this.props.data.customClassName || 'col-md-4';

        return (
            <div>
                <div className="form-group">
                    <label className="col-md-2 control-label">{this.props.data.name}:</label>
                    <div className={customClassName}>
                        <textarea
                            id={this.props.data.id}
                            className="form-control"
                            rows="7"
                            placeholder={this.props.data.placeholder}
                            value={this.props.data.elementValue}
                        />
                    </div>
                </div>
            </div>
        )
    }
});

var UserButton = React.createClass({
    render: function () {
        var customClassName = this.props.data.customClassName || 'col-md-2 col-md-offset-4';

        return (
            <div className={customClassName}>
                <a id={this.props.data.id} className="btn btn-primary center-block" href={this.props.data.url}>{this.props.data.value}</a>
            </div>
        )
    }
});

/**
 * Старница настроек собственника.
 * Здесь собственник задает все необходимые параметры своей недвижимости.
 * У одного пользователя может быть только ОДИН объект недвижимости.
 */
module.exports = React.createClass({
    getInitialState: function () {
        return {data: {}};
    },
    componentDidMount: function (rootNode) {
        var that = this;


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

        var changeDataStateValue = function (property, value) {
            var map = {};
            map[property] = value;

            that.setState({
                data: $.extend(that.state.data, map)
            });
        };

        var mapOptions = {
            center: {lat: -34.397, lng: 150.644},
            zoom: 16
        };

        var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);


        var marker = null;

        var centerMapAndSetMarker = function (lat, lng) {
            var currentPlace = {
                location: {
                    lat: lat,
                    lng: lng
                }
            };
            var latLng = new google.maps.LatLng(currentPlace.location.lat, currentPlace.location.lng);
            map.setCenter(latLng);
            if (marker != null) {
                marker.setMap(null);
            }
            marker = new google.maps.Marker({
                position: latLng,
                map: map,
                title: "Объект для сдачи"
            });
        };

        var autocomplete = new google.maps.places.Autocomplete(document.getElementById('addressInput'));
        google.maps.event.addListener(autocomplete, 'place_changed', function () {
            var place = autocomplete.getPlace();
            var addressComponents = place['address_components'];

            var location = {
                latitude: place['geometry']['location'].lat(),
                longitude: place['geometry']['location'].lng()
            };

            console.log(location);

            changeDataStateValue('location', location);

            changeDataStateValue('address', {
                city: getCity(addressComponents),
                country: getAddressComponentOfTypeOrNull(addressComponents, 'COUNTRY_LONG'),
                country_code: getAddressComponentOfTypeOrNull(addressComponents, 'COUNTRY'),
                county: getAddressComponentOfTypeOrNull(addressComponents, 'STATE'),
                district: getAddressComponentOfTypeOrNull(addressComponents, 'DISTRICT'),
                street_address: buildAddress(addressComponents),
                zip_code: getAddressComponentOfTypeOrNull(addressComponents, 'ZIP'),
                formatted_address: place['formatted_address']
            });


            centerMapAndSetMarker(place.geometry.location.lat(), place.geometry.location.lng());
        });

        $('#typeOfRent').on('change', function () {
            console.log('Type of rent changed');
            var selectedValue = $('#typeOfRent').find('option:selected').val();
            console.log('Selected: ' + selectedValue);
            changeDataStateValue('type_of_rent', selectedValue);
        });

        $('#rentalFee').bind('input propertychange', function () {
            var value = $('#rentalFee').val();
            console.log('changed to: ' + value);
            changeDataStateValue('rental_fee', value);
        });

        $('#feePeriod').on('change', function () {
            console.log('Type of rent changed');
            var selectedValue = $('#feePeriod').find('option:selected').val();
            console.log('Selected: ' + selectedValue);
            changeDataStateValue('fee_period', selectedValue);
        });

        $('#roomCount').bind('input propertychange', function () {
            var value = $('#roomCount').val();
            console.log('changed to: ' + value);
            changeDataStateValue('room_count', value);
        });

        $('#floorNumber').bind('input propertychange', function () {
            var value = $('#floorNumber').val();
            console.log('changed to: ' + value);
            changeDataStateValue('floor_number', value);
        });

        $('#floorsTotal').bind('input propertychange', function () {
            var value = $('#floorsTotal').val();
            console.log('changed to: ' + value);
            changeDataStateValue('floors_total', value);
        });

        $('#area').bind('input propertychange', function () {
            var value = $('#area').val();
            console.log('changed to: ' + value);
            changeDataStateValue('area', value);
        });

        $('#description').bind('input propertychange', function () {
            var value = $('#description').val();
            console.log('changed to: ' + value);
            changeDataStateValue('description', value);
        });

        var loadApartment = function () {
            $.blockUI({message: 'Loading...'});
            $.ajax({
                url: '/rest/users/apartment',
                dataType: 'json',
                type: 'GET',
                beforeSend: function (request) {
                    request.setRequestHeader("Authorization", "Basic " + Auth.userNamePasswordHardCode);
                },
                success: function (data) {
                    that.setState({data: data});
                    centerMapAndSetMarker(data.location.latitude, data.location.longitude);
                    $.unblockUI();
                }.bind(that),
                error: function (xhr, status, err) {
                    if (xhr.status == '404') {
                        that.setState({data: {}})
                    } else {
//                        console.error('/rest/apartment', status, err.toString());
                        alert('Service unavailable');
                    }
                    $.unblockUI();
                }.bind(that)
            });
        };

        var validateForm = function () {
            console.log('validating form');
            var data = that.state.data;
            console.log(data);

            var Assert = Validator.Assert;
            var Constraint = Validator.Constraint;
            var validator = new Validator.Validator();
            var constraintMap = {};

            constraintMap['location'] = [new Assert().NotNull()];
            constraintMap['address'] = [new Assert().NotNull()];
            constraintMap['type_of_rent'] = [new Assert().NotNull(), new Assert().Choice(['LONG_TERM', 'SHORT_TERM'])];
            constraintMap['fee_period'] = [new Assert().NotNull(), new Assert().Choice(
                ['HOURLY',
                    'DAILY',
                    'WEEKLY',
                    'MONTHLY']
            )];
            constraintMap['rental_fee'] = [new Assert().NotNull(), new Assert().GreaterThan(0)];
            constraintMap['room_count'] = [new Assert().NotNull(), new Assert().GreaterThan(0)];
            constraintMap['floor_number'] = [new Assert().NotNull(), new Assert().GreaterThan(0)];
            constraintMap['floors_total'] = [new Assert().NotNull(), new Assert().GreaterThan(0)];
            constraintMap['area'] = [new Assert().NotNull(), new Assert().Required()];
            constraintMap['description'] = [new Assert().Length({max: 2000})];

            var fieldToFormIdMap = {
                'address': 'addressInput',
                'type_of_rent': 'typeOfRent',
                'rental_fee': 'rentalFee',
                'fee_period': 'feePeriod',
                'room_count': 'roomCount',
                'floor_number': 'floorNumber',
                'floors_total': 'floorsTotal',
                'area': 'area',
                'description': 'description'
            };

            var defaultValueMap = {
                'location': null,
                'address': null,
                'type_of_rent': null,
                'rental_fee': null,
                'fee_period': null,
                'room_count': null,
                'floor_number': null,
                'floors_total': null,
                'area': null,
                'description': ''
            };

            var fieldToErrorMessageMap = {
                'address': 'Необходимо указать адрес',
                'type_of_rent': 'Необходимо указать тип сдачи',
                'rental_fee': 'Необходимо указать цену сдачи',
                'fee_period': 'Период оплаты обязателен',
                'room_count': 'Количество комнат обязательно',
                'floor_number': 'Номер этажа обязателен',
                'floors_total': 'Количество этажей обязателньно',
                'area': 'Необходимо указать площадь',
                'description': 'Описание не должно превышать 2000 символов'
            };

            //hide container
            var errorMessagesContainer = $('#errorMessages');

            errorMessagesContainer.hide();
            errorMessagesContainer.empty();
            //remove all appended child nodes

            var mergedMap = $.extend(defaultValueMap, data);

            var constraint = new Constraint(constraintMap, {strict: true});

            var validationResult = validator.validate(mergedMap, constraint);

            console.log(validationResult);

            var validationSucceeded = validationResult === true;

            if (!validationSucceeded) {
                var ul = $('<ul></ul>');

                var errorMessagesDivElements = _.map(validationResult, function (val, k) {
                    return fieldToErrorMessageMap[k];
                }).filter(function (v) {
                    return v;
                }).map(function (errorMessage) {
                    var li = $('<li></li>');
                    li.html(errorMessage);
                    return li;
                });


                _.each(errorMessagesDivElements, function (v) {
                    console.log(v);
                    v.appendTo(ul);
                });
                console.log(ul);
                ul.appendTo(errorMessagesContainer);
                errorMessagesContainer.show();
            } else {
                errorMessagesContainer.hide();
            }

            return validationSucceeded;
        };

        var saveApartment = function () {
            if (!validateForm()) return;

            $.blockUI({message: 'Loading...'});
            $.ajax({
                url: '/rest/users/apartment',
                dataType: 'json',
                type: 'POST',
                data: JSON.stringify(that.state.data),
                contentType: 'application/json; charset=utf-8',
                beforeSend: function (request) {
                    request.setRequestHeader("Authorization", "Basic " + Auth.userNamePasswordHardCode);
                },
                success: function (data) {
                    loadApartment();
                    $.unblockUI();
//                    that.setState({data: data});
                }.bind(that),
                error: function (xhr, status, err) {
//                    console.error('/rest/apartment', status, err.toString());
                    $.unblockUI();
                }.bind(that)
            });
        };

        var deleteApartment = function () {
            $.blockUI({message: 'Loading...'});
            $.ajax({
                url: '/rest/users/apartment',
//                dataType: 'json',
                type: 'DELETE',
//                contentType: 'application/json; charset=utf-8',
                beforeSend: function (request) {
                    request.setRequestHeader("Authorization", "Basic " + Auth.userNamePasswordHardCode);
                },
                success: function (data) {
                    loadApartment();
                    $.unblockUI();
//                    that.setState({data: data});
                }.bind(that),
                error: function (xhr, status, err) {
//                    console.error('/rest/apartment', status, err.toString());
                    alert('Service unavailable');
                    $.unblockUI();
                }.bind(that)
            });
        };

        loadApartment();

        $('#saveApartmentBtn').on('click', function () {
            console.log('saving data:');
            console.log(that.state.data);
            if (that.state.data.id) {
                alert('Вы уже сохраняли форму. Нельзя изменить данные.');
            } else {
                saveApartment();
            }
        });

        $('#deleteApartmentBtn').on('click', function () {
            deleteApartment();
        });
    },
    render: function () {
        var data = this.state.data || {};

        var addressProp = {
            id: 'addressInput',
            name: 'Адрес',
            placeholder: (data['address'] ? data['address']['formatted_address'] : null) || 'Начните печатать...',
            customClassName: 'col-md-8'
        };
        var rentTypeProp = {
            name: 'Тип сдачи',
            id: 'typeOfRent',
            defaultDescription: 'Выберите тип сдачи',
            keyValuePairs: [
                ['LONG_TERM', 'Долгосрочная'],
                ['SHORT_TERM', 'Краткосрочная']
            ],
            customClassName: 'col-md-8',
            selectedValue: data['type_of_rent']
        };

        var rentalFeeProp = {
            id: 'rentalFee',
            name: 'Плата',
            customClassName: 'col-md-8',
            elementValue: data['rental_fee']
        };
        var feePeriodProp = {
            id: 'feePeriod',
            name: 'Интервал оплаты',
            defaultDescription: 'Минимальный период сдачи',
            keyValuePairs: [
                ['HOURLY', 'Час'],
                ['DAILY', 'День'],
                ['WEEKLY', 'Неделя'],
                ['MONTHLY', 'Месяц']
            ],
            customClassName: 'col-md-8',
            selectedValue: data['fee_period']
        };

        var roomCount = {
            id: 'roomCount',
            name: 'Количество комнат',
            placeholder: '2',
            customClassName: 'col-md-8',
            elementValue: data['room_count']
        };
        var floorNumber = {
            id: 'floorNumber',
            name: 'Этаж',
            placeholder: '1',
            customClassName: 'col-md-8',
            elementValue: data['floor_number']
        };
        var floorsTotal = {
            id: 'floorsTotal',
            name: 'Всего этажей',
            placeholder: '9',
            customClassName: 'col-md-8',
            elementValue: data['floors_total']
        };
        var area = {
            id: 'area',
            name: 'Площадь',
            placeholder: '42 м2',
            customClassName: 'col-md-8',
            elementValue: data['area']
        };

        var descriptionProp = {
            id: 'description',
            name: 'Описание',
            customClassName: 'col-md-8',
            elementValue: data['description']
        };

        var submitButton = {id: 'saveApartmentBtn', value: 'Сохранить', customClassName: 'col-md-4 col-md-offset-4'};
        var deleteButton = {
            id: 'deleteApartmentBtn',
            value: 'Удалить(тест)',
            customClassName: 'col-md-4 col-md-offset-4'
        };

        var styles = {
            width: '300px',
            height: '300px'
        };

        var errorMessageStyles = {
            display: 'none'
        };

        return (
            <div className="col-md-9">
                <div className="panel">
                    <div className="panel-body">
                        <h4>Собственность</h4>

                        <br/>

                        <div id="errorMessages" className="alert alert-danger" role="alert" style={errorMessageStyles}>
                        </div>

                        <div className="row">
                            <div className="col-md-7" >
                                <form className="form-horizontal" role="form">
                                    <UserProperty data={addressProp}/>
                                    <UserSelect data={rentTypeProp}/>
                                    <UserProperty data={rentalFeeProp}/>
                                    <UserSelect data={feePeriodProp}/>
                                    <UserProperty data={roomCount}/>
                                    <UserProperty data={floorNumber}/>
                                    <UserProperty data={floorsTotal}/>
                                    <UserProperty data={area}/>
                                    <UserText data={descriptionProp}/>

                                    <UserButton data={submitButton}/>

                                    <br/>
                                    <br/>

                                    <UserButton data={deleteButton}/>
                                </form>
                            </div>
                            <div id="map-canvas" className="col-md-4" style={styles}></div>
                        </div>


                    </div>
                </div>
            </div>
        )
    }
});