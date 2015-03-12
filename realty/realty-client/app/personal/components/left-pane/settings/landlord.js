/**
 * Настройки собственности юзера
 */
//var $ = require('jquery');
//require('blockui');
var JSON = require('JSON2');
var Validator = require('validator.js');
var _ = require('underscore');
var React = require('react');
var Modal = require('react-bootstrap/Modal');
var ModalTrigger = require('react-bootstrap/ModalTrigger');
var Button = require('react-bootstrap/Button');
//var ReactBootstrap = require('react-bootstrap/main');

var AuthStore = require('../../../../shared/stores/AuthStore');
var AddressUtils = require('../../../../shared/common/AddressUtils');
var ApartmentStore = require('../../../../shared/stores/ApartmentStore');
var ApartmentActions = require('../../../../shared/actions/ApartmentActions');
var NavActions = require('../../../../shared/actions/NavActions');
var Translations = require('../../../../shared/l10n/Translations');
var assign = require('object-assign');

var Dropzone = require("dropzone");

function changeData(ctxt, data) {
    var target = {
        data: assign(ctxt.state.data, data),
        transient: ctxt.state.transient
    };
    ctxt.setState(assign(ctxt.state, target));
}

function changeTransient(ctxt, transient, cb) {
    var target = {
        transient: assign(ctxt.state.transient, transient),
        data: ctxt.state.data
    };
    ctxt.setState(assign(ctxt.state, target), cb);
}

var ApartmentPhoto = React.createClass({
    render: function () {
        var style = {
            border: '1px solid black'
        };
        return (
            <li>
                <div>
                    <img src={this.props.photo.small_thumbnail_url} height="100" style={style} className="clickable" onClick={this.props.onSelect && this.props.onSelect.bind(this, this.props.photo)} />
                    <br/>
                    <a href="javascript:void(0)" onClick={this.props.onDelete && this.props.onDelete.bind(this, this.props.photo.guid)}>Remove</a>
                </div>
            </li>
        );
    }
});

var ApartmentInfoChangeRequestForm = React.createClass({
    getInitialState: function () {
        return {
            data: ApartmentStore.getMyProfile()
        };
    },

    componentDidMount: function () {
        var that = this;
        var autocomplete = new google.maps.places.Autocomplete(document.getElementById('addressInputChange'));
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

            changeData(that, delta);
        });
    },

    _onSave: function () {
        //TODO: validate
        ApartmentActions.sendApartmentRentInfo(this.state.data);
    },

    _onChange: function (event) {
        if (!event) {
            return;
        }

        var diffObj = {};
        diffObj[event.target.name] = event.target.value;

        console.log('diff obj');
        console.log(diffObj);

        console.log('old state');
        console.log(this.state.data);

        var newState = {data: diffObj};

        console.log('new state');
        console.log(newState.data);

        changeData(this, diffObj);
    },

    render: function () {

        var data = this.state.data || {};

        var addressPreviewProp = {
            id: 'addressPreviewChange',
            name: 'Выбранный адрес',
            previewValue: (data['address'] ? data['address']['formatted_address'] : null) || '',
            customClassName: 'col-md-8',
            labelClass: 'col-md-4'
        };

        var addressProp = {
            id: 'addressInputChange',
            name: 'Адрес',
            placeholder: 'Введите адрес ...',
            customClassName: 'col-md-8',
            labelClass: 'col-md-4'
        };

        var roomCount = {
            id: 'roomCountChange',
            name: 'Количество комнат',
            placeholder: '',
            customClassName: 'col-md-8',
            elementName: 'room_count',
            elementValue: data['room_count'],
            labelClass: 'col-md-4'
        };
        var floorNumber = {
            id: 'floorNumberChange',
            name: 'Этаж',
            placeholder: '',
            customClassName: 'col-md-8',
            elementName: 'floor_number',
            elementValue: data['floor_number'],
            labelClass: 'col-md-4'
        };
        var floorsTotal = {
            id: 'floorsTotalChange',
            name: 'Всего этажей',
            placeholder: '',
            customClassName: 'col-md-8',
            elementName: 'floors_total',
            elementValue: data['floors_total'],
            labelClass: 'col-md-4'
        };
        var area = {
            id: 'areaChange',
            name: 'Площадь',
            placeholder: '',
            customClassName: 'col-md-8',
            elementName: 'area',
            elementValue: data['area'],
            labelClass: 'col-md-4'
        };

        var submitButton = {
            id: 'saveApartmentChangeBtn',
            value: 'Отправить',
            customClassName: 'col-md-4 col-md-offset-4'
        };

        return (
            <div>
                <p className="alert alert-info">
                    Наша цель - не стать очередным сервисом для риэлторов
                    <br/>
                    Поэтому мы проверяем все изменения данных о квартире.
                    <br/>
                    <br/>
                    После проверки мы опубликуем измененное объявление.
                    <br/>
                    <br/>
                    До этого времени объявление будет отображаться в неизмененном виде.
                    <br/>
                </p>
                <br/>
                <form className="form-horizontal" role="form">
                    <div className="row">
                        <div className="col-md-12" >
                            <UserProperty data={roomCount} onChange={this._onChange} />
                            <UserProperty data={floorNumber} onChange={this._onChange} />
                            <UserProperty data={floorsTotal} onChange={this._onChange} />
                            <UserProperty data={area} onChange={this._onChange} />
                            <UserProperty data={addressProp} />
                            <UserPreview data={addressPreviewProp}/>

                            <UserButton data={submitButton} onClick={this._onSave}/>
                        </div>
                    </div>
                </form>
            </div>
        );
    }
});

var ApartmentInfoChangeRequestModal = React.createClass({
    getInitialState: function () {
        return {visible: false};
    },

    componentDidMount: function () {
        console.log('modal mounted');
        ApartmentStore.addChangeListener(this._onLoad);
    },

    componentWillUnmount: function () {
        ApartmentStore.removeChangeListener(this._onLoad);
        console.log('modal will unmount');
    },

    _onLoad: function () {
        var hideFunc = this.props.onRequestHide;
        if (hideFunc) {
            hideFunc();
        }
    },

    render: function () {
        return (
            <div>
                <Modal {...this.props} title="Запрос на изменение данных о квартире" animation={false} style={{borderBottom: 'none'}}>
                    <div className="modal-body">
                        <ApartmentInfoChangeRequestForm/>
                    </div>
                </Modal>
            </div>
        );
    }
});

var ApartmentInfoChangeRequestButton = React.createClass({
    render: function () {
        var show = this.props.show;

        var elementOrEmpty = show ?
            (
                <div>
                    <ModalTrigger modal={<ApartmentInfoChangeRequestModal />}>
                        <Button bsStyle="default" bsSize="medium">Изменить данные о квартире</Button>
                    </ModalTrigger>
                </div>
            )
            : "";

        return (
            <div>
            {elementOrEmpty}
            </div>
        );
    }
});

var ApartmentPhotoPreview = React.createClass({
    render: function () {
        var style = {
            maxWidth: '75%'
        };

        return (
            <div>
                <img src={this.props.photo.full_picture_url} style={style}/>
            </div>
        );
    }
});

var ApartmentPhotoList = React.createClass({
    render: function () {
        var _photos = this.props.photos || [];
        var onDelete = this.props.onDelete;
        var onSelect = this.props.onSelect;
        var photos = _photos.map(function (photo) {
            return <ApartmentPhoto key={photo.guid} photo={photo} onDelete={onDelete} onSelect={onSelect}/>
        });
        var style = {
            display: "inline-block"
        };

        return (
            <ul className="list-inline">
                {photos}
            </ul>
        );
    }
});

var ApartmentPhotosBlock = React.createClass({
    render: function () {
        var photos = this.props.photos || [];
        var selectedPhoto = this.props.selectedPhoto;
        var photoPreviewOrZero = selectedPhoto ? (<ApartmentPhotoPreview photo={selectedPhoto}/>) : null;

        return (
            <div>
                <ApartmentPhotoList photos={photos} onDelete={this.props.onDelete} onSelect={this.props.onSelect} />
                    {photoPreviewOrZero}
            </div>
        );
    }
});

var UserPreview = React.createClass({
    render: function () {
        var customClassName = this.props.data.customClassName || 'col-md-4';
        var label = this.props.data.labelClass || "col-md-3 " + " control-label";

        return (
            <div>
                <div className="form-group">
                    <label className={label}>{this.props.data.name}</label>
                    <div className={customClassName}>
                        <div className="control-label">
                            <strong>{this.props.data.previewValue}</strong>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

var UserProperty = React.createClass({
    render: function () {
        var customClassName = this.props.data.customClassName;
        var label = this.props.data.labelClass + " control-label";

        return (
            <div>
                <div className="form-group">
                    <label className={label}>{this.props.data.name}</label>
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

var UserCheckbox = React.createClass({
    render: function () {
        var customClassName = this.props.data.customClassName || 'col-md-4';

        return (
            <div>
                <div className="form-group">
                    <label className="col-md-3 control-label">
                        {this.props.data.name}
                    </label>
                    <div className={customClassName}>
                        <input
                            id={this.props.data.id}
                            type="checkbox"
                            readOnly={this.props.readOnly}
                            name={this.props.data.elementName}
                            onChange={this.props.onChange}
                            checked={this.props.data.checked}
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
                    <label className="col-md-3 control-label">{this.props.data.name}</label>
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
                    <label className="col-md-3 control-label">{this.props.data.name}</label>
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
        var customClassName = this.props.data.customClassName;
        var type = 'btn center-block ' + (this.props.data.type || 'btn-primary');

        return (
            <div className={customClassName}>
                <a id={this.props.data.id} className={type} href={this.props.data.url} onClick={this.props.onClick}>{this.props.data.value}</a>
            </div>
        )
    }
});

var ApartmentWelcomeScreen = React.createClass({
    propTypes: {
        onNextStepSelected: React.PropTypes.func
    },

    render: function () {
        var onNextStepSelected = this.props.onNextStepSelected;

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Собственность</h4>
                        <br/>
                        <div className="row">
                            <div className="col-md-offset-1">
                                <p>
                                    У вас пока что нет ни одного объявления о сдаче жилья в аренду.
                                    <br/>
                                    Являясь собственником жилья вы можете зарабатывать деньги сдавая его в аренды.
                                    <br/>
                                </p>
                                <br/>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-md-offset-1">
                                <p>
                                    <a href="javascript:void(0)" className="btn btn-primary" onClick={onNextStepSelected}>
                                        <i className="glyphicon glyphicon-plus" style={{color: 'white'}}></i>
                                        <b>Добавить объявление</b>
                                    </a>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

/**
 * Старница настроек собственника.
 * Здесь собственник задает все необходимые параметры своей недвижимости.
 * У одного пользователя может быть только ОДИН объект недвижимости.
 */

var _marker = null;
var _map = null;
var _mapCenter = null;
var _uploadFileNameToGuidMap = {};
var _dropZone = null;

module.exports = React.createClass({
    getInitialState: function () {
//        console.log('get initial state');
        return {
            data: ApartmentStore.getMyProfile(),
            transient: {
                confirmedToAddApartment: false
            }
        };
    },

    _resetExternalDepState: function () {
        _marker = null;
        _map = null;
        _mapCenter = null;
        _uploadFileNameToGuidMap = {};
        _dropZone = null;
    },

    componentDidMount: function () {
        NavActions.navigateToLandlord();
        console.log('Navigated to landlord');

        Dropzone.autoDiscover = false;

        this._resetExternalDepState();//refresh state because you might transition to other ui elements and back. so you need to reset it.

        //        console.log('component mounted');
        ApartmentStore.addChangeListener(this._onLoad);

        ApartmentActions.loadMyApartment();

        if (!this._formCouldBeDisplayed()) {
            //perform no initialization
            return;
        } else {
            this.initAll(this);
        }
    },

    initAll: function (ctxt) {
        console.log('Initializing');
        var that = ctxt;
        if (!_dropZone) {
            _dropZone = new Dropzone('#my-awesome-dropzone', {
                maxFilesize: 5,
//            maxFiles: 10,
                addRemoveLinks: true,
                acceptedFiles: ".png, .jpg, .jpeg, .jpe, .gif, .bmp, .tif, .tiff",
                dictDefaultMessage: "Перетащите картинки сюда, что бы добавить их",
                url: '/rest/users/apartment/pictures/temp',
                headers: {
                    "Authorization": "Basic " + AuthStore.getAuthHeader()
                },
                init: function () {
                    this.on("removedfile", function (file) {
                        var guid = _uploadFileNameToGuidMap[file.name];
                        if (guid) {
                            that._onPhotoDelete(guid);
                        }
                    });
                    this.on("success", function (file, data) {
                        console.log('Complete file upload');
                        console.log(file);
                        console.log('Data');
                        console.log(data);
                        if (data && data.guid) {
                            _uploadFileNameToGuidMap[file.name] = data.guid;
                            //change state of added photos
                            var newState = assign({}, that.state.data);
                            newState.added_photos_guids.push(data.guid);
                            changeData(that, newState);
                        }
                    });
                }
            });
        }


        if (!_map) {
            //initialize google maps
            var mapOptions = {
                center: {lat: 55.752129, lng: 37.617531},
                zoom: 16
            };

            _map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

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

                changeData(that, delta);

                that.centerMapAndSetMarker(place.geometry.location.lat(), place.geometry.location.lng());
            });
            that.setCenterOnMapIfPossible(_mapCenter);
        }
    },

    componentWillUnmount: function () {
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
        _mapCenter = latLng;
        this.setCenterOnMapIfPossible(latLng);
    },

    setCenterOnMapIfPossible: function (latLng) {
        if (_map && latLng) {
            _map.setCenter(latLng);
            if (_marker != null) {
                _marker.setMap(null);
            }
            _marker = new google.maps.Marker({
                position: latLng,
                map: _map,
                title: "Объект для сдачи"
            });
        }
    },

    _onLoad: function () {
        console.log('on load');
        //clear this map state
        _uploadFileNameToGuidMap = {};
        var newState = ApartmentStore.getMyProfile();

        this.setState(assign({}, this.state, {
            data: newState,
            transient: {
                selectedPhoto: (newState.photos.length > 0 ? newState.photos[0] : null)
            }
        }), this._reInitIfNeededCb(this));

        var data = this.state.data;
        if (data && data.location) {
            this.centerMapAndSetMarker(data.location.latitude, data.location.longitude);
        }

        if (_dropZone) {
            console.log('removing all files');
            _dropZone.removeAllFiles();
        }
    },

    _onSave: function () {
        if (!this.validateForm()) return;
        if (this.state.data.id) {
            ApartmentActions.updateApartmentRentInfo(assign({}, this.state.data));
        } else {
            ApartmentActions.createApartment(assign({}, this.state.data));
        }


    },

    _onDelete: function () {
        this._resetExternalDepState();
        ApartmentActions.deleteMyApartment();
    },

    validateForm: function () {
        var data = this.state.data;

        var Assert = Validator.Assert;
        var Constraint = Validator.Constraint;
        var validator = new Validator.Validator();
        var constraintMap = {};

        constraintMap['location'] = [new Assert().NotNull()];
        constraintMap['address'] = [new Assert().NotNull()];
        constraintMap['type_of_rent'] = [new Assert().NotNull(), new Assert().Choice(['LONG_TERM', 'SHORT_TERM'])];
        constraintMap['fee_period'] = [new Assert().NotNull(), new Assert().Choice(
            ['DAILY',
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

    _onChange: function (event) {
        if (!event) {
            return;
        }

        var diffObj = {};
        diffObj[event.target.name] = event.target.value;

        console.log('diff obj');
        console.log(diffObj);

        console.log('old state');
        console.log(this.state.data);

        var newState = {data: diffObj};

        console.log('new state');
        console.log(newState.data);

        changeData(this, diffObj);
    },

    _onChangeCheckbox: function (event) {
        if (!event) {
            return;
        }

        var diffObj = {};
        diffObj[event.target.name] = event.target.checked;

        console.log('diff obj');
        console.log(diffObj);

        console.log('old state');
        console.log(this.state.data);

        var newState = {data: diffObj};

        console.log('new state');
        console.log(newState.data);

        changeData(this, diffObj);
    },

    _onPublishedChangedCb: function (boolFlag) {
        var self = this;

        return function () {
            var diffObj = {};
            diffObj['published'] = boolFlag;

            console.log('diff obj');
            console.log(diffObj);

            console.log('old state');
            console.log(self.state.data);

            var newState = {data: diffObj};

            console.log('new state');
            console.log(newState.data);

            changeData(self, diffObj);
            self._onSave();
        };
    },

    _onPhotoSelected: function (photo) {
        console.log("Photo seleceted:");
        console.log(photo);
//        changeTransient(this, assign(this.state.transient, {selectedPhoto: {photo:{full_picture_url: 'images/spin.gif' }}}))
        changeTransient(this, assign(this.state.transient, {selectedPhoto: photo}))
    },

    _onPhotoDelete: function (guid) {
        console.log('Deleting ');
        console.log(guid);
        var oldState = this.state.data;
        var newState = assign({}, oldState);

        var _filterFunc = function (photoGuid) {
            var result = photoGuid != guid;
            console.log("Value for compare: " + photoGuid + "; compare with: " + guid + " = " + result);
            return result;
        };

        var _photosFilterFunc = function (photo) {
            return _filterFunc(photo.guid);
        };

        //remove guid for added photos
        console.log('old added photos:');
        console.log(oldState.added_photos_guids);

        newState.added_photos_guids = _.filter(oldState.added_photos_guids, _filterFunc);

        console.log('new added photos:');
        console.log(newState.added_photos_guids);
        //remove from photos list
        newState.photos = _.filter(oldState.photos, _photosFilterFunc);

        var photoGuids = _.map(oldState.photos, function (it) {
            return it.guid;
        });

        console.log("Photo guids:");
        console.log(photoGuids);

        if (_.contains(photoGuids, guid)) {
            newState.deleted_photos_guids.push(guid);
        }

        if (_.contains(oldState.added_photos_guids, guid)) {
            newState.added_photos_guids = _.filter(oldState.added_photos_guids, _filterFunc);
        }


        changeData(this, newState);
    },

    _reInitIfNeededCb: function (self) {
        return function () {
            console.log('UI re-rendered');
            if (!self._formCouldBeDisplayed()) {
                console.log('We can not init 3-rd party dependency.');
                return;
            }

            self.initAll(self);
        }
    },

    _onNextStepSelected: function () {
        //changeTransient(this, assign(this.state.transient, {confirmedToAddApartment: true}), this.initAll);
        var self = this;
        changeTransient(this, assign(this.state.transient, {confirmedToAddApartment: true}), this._reInitIfNeededCb(self));
    },

    _formCouldBeDisplayed: function () {
        var data = this.state.data || {};
        var transient = this.state.transient;
        var saved = !!data.id;

        return saved || transient.confirmedToAddApartment;
    },

    render: function () {
        var data = this.state.data || {};
        var transient = this.state.transient;
        var saved = !!data.id;

        if (!this._formCouldBeDisplayed()) {
            return <ApartmentWelcomeScreen onNextStepSelected={this._onNextStepSelected}/>;
        }

        var readOnly = saved;

        var addressPreviewProp = {
            id: 'addressPreview',
            name: 'Выбранный адрес',
            previewValue: (data['address'] ? data['address']['formatted_address'] : null) || '',
            customClassName: 'col-md-10',
            labelClass: 'col-md-2'
        };

        var addressProp = {
            id: 'addressInput',
            name: 'Адрес',
            placeholder: 'Введите адрес ...',
            customClassName: 'col-md-10',
            labelClass: 'col-md-2'
        };

        var rentTypeProp = {
            name: 'Тип аренды',
            id: 'typeOfRent',
            defaultDescription: 'Выберите тип аренды',
            keyValuePairs: [
                ['LONG_TERM', Translations['ru']['LONG_TERM']],
                ['SHORT_TERM', Translations['ru']['SHORT_TERM']]
            ],
            customClassName: 'col-md-8',
            elementName: 'type_of_rent',
            selectedValue: data['type_of_rent']
        };

        var rentalFeeProp = {
            id: 'rentalFee',
            name: 'Цена',
            customClassName: 'col-md-8',
            elementName: 'rental_fee',
            elementValue: data['rental_fee'],
            labelClass: 'col-md-3'
        };
        var feePeriodProp = {
            id: 'feePeriod',
            name: 'Оплата',
            defaultDescription: 'Выберите период оплаты',
            keyValuePairs: [
                ['DAILY', Translations['ru']['DAILY']],
                ['WEEKLY', Translations['ru']['WEEKLY']],
                ['MONTHLY', Translations['ru']['MONTHLY']]
            ],
            customClassName: 'col-md-8',
            elementName: 'fee_period',
            selectedValue: data['fee_period']
        };

        var roomCount = {
            id: 'roomCount',
            name: 'Комнат',
            placeholder: '',
            customClassName: 'col-md-8',
            elementName: 'room_count',
            elementValue: data['room_count'],
            labelClass: 'col-md-4'
        };
        var floorNumber = {
            id: 'floorNumber',
            name: 'Этаж',
            placeholder: '',
            customClassName: 'col-md-8',
            elementName: 'floor_number',
            elementValue: data['floor_number'],
            labelClass: 'col-md-4'
        };
        var floorsTotal = {
            id: 'floorsTotal',
            name: 'Этажность',
            placeholder: '',
            customClassName: 'col-md-8',
            elementName: 'floors_total',
            elementValue: data['floors_total'],
            labelClass: 'col-md-4'
        };
        var area = {
            id: 'area',
            name: 'Площадь',
            placeholder: '',
            customClassName: 'col-md-8',
            elementName: 'area',
            elementValue: data['area'],
            labelClass: 'col-md-4'
        };

        var descriptionProp = {
            id: 'description',
            name: 'Описание',
            customClassName: 'col-md-8',
            elementName: 'description',
            elementValue: data['description']
        };

        var publishedProp = {
            id: 'published',
            name: 'Опубликовано',
            customClassName: 'col-md-4 col-md-offset-1',
            elementName: 'published',
            checked: data['published']
        };

        var deleteButton = {
            id: 'deleteApartmentBtn',
            value: 'Удалить',
            customClassName: 'col-md-2 col-md-offset-8',
            type: 'btn-danger'
        };

        var submitButton = {
            id: 'saveApartmentBtn',
            value: 'Сохранить',
            customClassName: 'col-md-2',
            type: 'btn-primary'
        };

        var styles = {
            margin: '0 auto',
            width: '100%',
            minWidth: '300px',
            height: '300px'
        };

        var errorMessageStyles = {
            display: 'none'
        };

        var onChangeIfNotSaved = saved ? null : this._onChange;


        var selectedPhoto = this.state.transient.selectedPhoto;
        var photoPreviewOrZero = selectedPhoto ? (<ApartmentPhotoPreview photo={selectedPhoto}/>) : null;

        var successOrDangerBlock = null;
        console.log('It\'s new apartment: ' + !saved);
        if (saved) {
            var success = data.published;

            if (success) {
                successOrDangerBlock = (
                    <div className="alert alert-success" role="alert" >
                        Ваша квартира отображается в поиске
                        <br/>
                        <br/>
                        <a href="javascript:void(0)" className="btn btn-danger" onClick={this._onPublishedChangedCb(false)}>Убрать из поиска</a>
                    </div>
                );
            } else {
                successOrDangerBlock = (
                    <div className="alert alert-danger" role="alert" >
                        Ваша квартира не участвует в поиске
                        <br/>
                        <br/>
                        <a href="javascript:void(0)" className="btn btn-success" onClick={this._onPublishedChangedCb(true)}>Добавить в поиск</a>
                    </div>
                );
            }
        }
        return (
            <div className="col-md-9">
                <div className="panel">
                    <div className="panel-body">
                        <h4>Собственность</h4>

                        <br/>

                        <div id="errorMessages" className="alert alert-danger" role="alert" style={errorMessageStyles}>
                        </div>

                        {successOrDangerBlock}

                        <form className="form-horizontal" role="form">
                            <div className="row">
                                <div className="col-md-6" >
                                    <UserSelect data={rentTypeProp} onChange={this._onChange} />
                                    <UserProperty data={rentalFeeProp} onChange={this._onChange} />
                                    <UserSelect data={feePeriodProp} onChange={this._onChange} />

                                    <UserText data={descriptionProp} onChange={this._onChange} />

                                </div>
                                <div className="col-md-6">
                                    <div id="map-canvas" style={styles}></div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-md-12">
                                    <h4>Данные о квартире</h4>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-md-4" >
                                    <UserProperty data={area} onChange={onChangeIfNotSaved} readOnly={readOnly}/>
                                    <UserProperty data={roomCount} onChange={onChangeIfNotSaved} readOnly={readOnly}/>
                                </div>
                                <div className="col-md-4">
                                    <UserProperty data={floorNumber} onChange={onChangeIfNotSaved} readOnly={readOnly}/>
                                    <UserProperty data={floorsTotal} onChange={onChangeIfNotSaved} readOnly={readOnly}/>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-md-8">
                                    <UserProperty data={addressProp} readOnly={saved}/>
                                    <UserPreview data={addressPreviewProp}/>
                                </div>
                                <div className="col-md-4">
                                    <ApartmentInfoChangeRequestButton show={readOnly}/>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-md-12">
                                    <h4>Фотографии</h4>
                                    <ApartmentPhotoList photos={data.photos} onDelete={this._onPhotoDelete} onSelect={this._onPhotoSelected} />
                                    <p>
                                        <div className="dropzone" id="my-awesome-dropzone"></div>
                                    </p>
                                </div>
                            </div>

                            <div className='row'>
                                <UserButton data={deleteButton} onClick={this._onDelete}/>
                                <UserButton data={submitButton} onClick={this._onSave}/>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        )

    }
});