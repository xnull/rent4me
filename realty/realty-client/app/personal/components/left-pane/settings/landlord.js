/**
 * Настройки собственности юзера
 */
//var $ = require('jquery');
//require('blockui');
var JSON = require('JSON2');
var Validator = require('validator.js');
var _ = require('underscore');
var React = require('react');
var Auth = require('../../../../shared/common/Auth');
var AddressUtils = require('../../../../shared/common/AddressUtils');
var ApartmentStore = require('../../../../shared/stores/ApartmentStore');
var ApartmentActions = require('../../../../shared/actions/ApartmentActions');
var assign = require('object-assign');

var Dropzone = require("dropzone");

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
                            readOnly={this.props.readOnly}
                            name={this.props.data.elementName}
                            onChange={this.props.onChange}
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
                        <select
                        id={this.props.data.id}
                        className="form-control"
                        value={selectedValue}
                        name={this.props.data.elementName}
                        onChange={this.props.onChange} >
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
                            name={this.props.data.elementName}
                            onChange={this.props.onChange}
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
                <a id={this.props.data.id} className="btn btn-primary center-block" href={this.props.data.url} onClick={this.props.onClick}>{this.props.data.value}</a>
            </div>
        )
    }
});

/**
 * Старница настроек собственника.
 * Здесь собственник задает все необходимые параметры своей недвижимости.
 * У одного пользователя может быть только ОДИН объект недвижимости.
 */

var _marker = null;
var _map = null;

module.exports = React.createClass({
    getInitialState: function() {
//        console.log('get initial state');
        return ApartmentStore.getMyProfile();
    },
    componentDidMount: function() {
        Dropzone.autoDiscover = false;
        var dropZone = new Dropzone('#my-awesome-dropzone', {
            maxFilesize: 5,
//            maxFiles: 10,
            acceptedFiles: ".png, .jpg, .jpeg, .jpe, .gif, .bmp, .tif, .tiff",
            dictDefaultMessage: "Перетащите картинки сюда, что бы добавить их",
            url: '/rest/users/apartment/pictures',
            headers: {
                "Authorization": "Basic " + Auth.getAuthHeader()
            }
        });

//        console.log('component mounted');
        ApartmentStore.addChangeListener(this._onLoad);

        //initialize google maps
        var mapOptions = {
            center: {lat: -34.397, lng: 150.644},
            zoom: 16
        };

        _map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

        var that = this;
        var autocomplete = new google.maps.places.Autocomplete(document.getElementById('addressInput'));
        google.maps.event.addListener(autocomplete, 'place_changed', function () {
            var place = autocomplete.getPlace();
            var addressComponents = place['address_components'];

            var location = {
                latitude: place['geometry']['location'].lat(),
                longitude: place['geometry']['location'].lng()
            };

//            console.log(location);

            var delta = {
                'location': location,
                'address': {
                    city: AddressUtils.getCity(addressComponents),
                    country: AddressUtils.getAddressComponentOfTypeOrNull(addressComponents, 'COUNTRY_LONG'),
                    country_code: AddressUtils.getAddressComponentOfTypeOrNull(addressComponents, 'COUNTRY'),
                    county: AddressUtils.getAddressComponentOfTypeOrNull(addressComponents, 'STATE'),
                    district: AddressUtils.getAddressComponentOfTypeOrNull(addressComponents, 'DISTRICT'),
                    street_address: AddressUtils.buildAddress(addressComponents),
                    zip_code: AddressUtils.getAddressComponentOfTypeOrNull(addressComponents, 'ZIP'),
                    formatted_address: place['formatted_address']
                }
            };

            that.setState(assign(that.state, delta));

            that.centerMapAndSetMarker(place.geometry.location.lat(), place.geometry.location.lng());
        });

        ApartmentActions.loadMyApartment();
    },

    componentWillUnmount: function() {
        ApartmentStore.removeChangeListener(this._onLoad);
    },

    centerMapAndSetMarker: function (lat, lng) {

        var currentPlace = {
            location: {
                lat: lat,
                lng: lng
            }
        };
        var latLng = new google.maps.LatLng(currentPlace.location.lat, currentPlace.location.lng);
        _map.setCenter(latLng);
        if (_marker != null) {
            _marker.setMap(null);
        }
        _marker = new google.maps.Marker({
            position: latLng,
            map: _map,
            title: "Объект для сдачи"
        });
    },

    _onLoad: function() {
//        console.log("On load apartment");
        var newState = ApartmentStore.getMyProfile();
//        console.log("New apartment state: "+JSON.stringify(newState));
//        console.log(newState);
        this.setState(newState);

        var data = this.state;
        if(data && data.location) {
            this.centerMapAndSetMarker(data.location.latitude, data.location.longitude);
        }
    },

    _onSave: function() {
        if (this.state.id) {
            alert('Вы уже сохраняли форму. Нельзя изменить данные.');
            return;
        }

        if(!this.validateForm()) return;

        ApartmentActions.save(assign({}, this.state));
    },

    _onDelete: function() {
        ApartmentActions.deleteMyApartment();
    },

    validateForm: function () {
//        console.log('validating form');
        var data = this.state;
//        console.log(data);

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
            'location': 'Необходимо указать координаты',
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

//        console.log(validationResult);

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
//                console.log(v);
                v.appendTo(ul);
            });
//            console.log(ul);
            ul.appendTo(errorMessagesContainer);
            errorMessagesContainer.show();
        } else {
            errorMessagesContainer.hide();
        }

        return validationSucceeded;
    },

    _onChange: function(event) {
        if(!event) {
//            console.log('no event specified');
            return;
        }

//        console.log('on change');
//        console.log(event);
//        console.log(event.target);
//        console.log(event.target.name);
//        console.log(event.target.value);

        var diffObj = {};
        diffObj[event.target.name] = event.target.value;

        var newState = assign(this.state, diffObj);

//        console.log(newState);
        this.setState(newState);
    },

    render: function () {
        var data = this.state || {};

        var addressPreviewProp = {
            id: 'addressPreview',
            name: 'Выбранный адрес',
            placeholder: (data['address'] ? data['address']['formatted_address'] : null) || '',
            customClassName: 'col-md-8'
        };

        var addressProp = {
            id: 'addressInput',
            name: 'Адрес',
            placeholder: 'Начните печатать...',
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
            elementName: 'type_of_rent',
            selectedValue: data['type_of_rent']
        };

        var rentalFeeProp = {
            id: 'rentalFee',
            name: 'Плата',
            customClassName: 'col-md-8',
            elementName: 'rental_fee',
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
            elementName: 'fee_period',
            selectedValue: data['fee_period']
        };

        var roomCount = {
            id: 'roomCount',
            name: 'Количество комнат',
            placeholder: '2',
            customClassName: 'col-md-8',
            elementName: 'room_count',
            elementValue: data['room_count']
        };
        var floorNumber = {
            id: 'floorNumber',
            name: 'Этаж',
            placeholder: '1',
            customClassName: 'col-md-8',
            elementName: 'floor_number',
            elementValue: data['floor_number']
        };
        var floorsTotal = {
            id: 'floorsTotal',
            name: 'Всего этажей',
            placeholder: '9',
            customClassName: 'col-md-8',
            elementName: 'floors_total',
            elementValue: data['floors_total']
        };
        var area = {
            id: 'area',
            name: 'Площадь',
            placeholder: '42 м2',
            customClassName: 'col-md-8',
            elementName: 'area',
            elementValue: data['area']
        };

        var descriptionProp = {
            id: 'description',
            name: 'Описание',
            customClassName: 'col-md-8',
            elementName: 'description',
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
                                    <UserProperty data={addressPreviewProp} readOnly={true}/>
                                    <UserProperty data={addressProp}/>
                                    <UserSelect data={rentTypeProp} onChange={this._onChange}/>
                                    <UserProperty data={rentalFeeProp} onChange={this._onChange}/>
                                    <UserSelect data={feePeriodProp} onChange={this._onChange}/>
                                    <UserProperty data={roomCount} onChange={this._onChange}/>
                                    <UserProperty data={floorNumber} onChange={this._onChange}/>
                                    <UserProperty data={floorsTotal} onChange={this._onChange}/>
                                    <UserProperty data={area} onChange={this._onChange}/>
                                    <UserText data={descriptionProp} onChange={this._onChange}/>

                                    <UserButton data={submitButton} onClick={this._onSave}/>

                                    <br/>
                                    <br/>

                                    <UserButton data={deleteButton} onClick={this._onDelete}/>
                                </form>
                            </div>
                            <div id="map-canvas" className="col-md-4" style={styles}></div>
                        </div>

                        <h4>Фотографии</h4>

                        <p>
                            <div className="dropzone"
                                    id="my-awesome-dropzone"></div>
                        </p>


                    </div>
                </div>
            </div>
        )
    }
});