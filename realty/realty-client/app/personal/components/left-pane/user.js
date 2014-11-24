/** @jsx React.DOM */

var UserComponent = React.createClass({
    render: function () {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <h4>Пользователь</h4>

                    <div className="well well-sm">
                        <div className="media">
                            <a className="thumbnail pull-left" href="#">
                                <img className="media-object" src="//placehold.it/80"/>
                            </a>

                            <div className="media-body">
                                <h4 className="media-heading">John Doe</h4>

                                <br/>

                                <div className="col-md-12">
                                    <a className="btn btn-default center-block" href="#/user">Настройки</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-6 col-md-offset-6">
                        <a className="btn btn-success" href="#/user/landlord">Я собственник</a>
                    </div>

                </div>
            </div>
        );
    }
});

var LandlordSettings = React.createClass({
    getInitialState: function() {
        return {data: {}};
    },
    componentDidMount: function (rootNode) {
        var that = this;


        //TODO: move this address parsing related stuff in some kind of generic helper library
        var getShortName = function(addressComponent){
            return addressComponent['short_name'];
        };

        var getLongName = function(addressComponent){
            return addressComponent['long_name'];
        };

        var getCity = function(addressComponents) {
            addressComponents = addressComponents || [];


            console.log(addressComponents);

            var district = null;
            var area = null;
            var country = null;

            var len = addressComponents.length;

            var i;
            for(i = 0; i < len; i++) {
                var addressComponent = addressComponents[i];

                var types = addressComponent['types'] || [];

                var typeLen = types.length;


                for(var j = 0; j < typeLen; j++) {
                    var type = types[j].toUpperCase();

                    if( type == 'LOCALITY'){
                        return getLongName(addressComponent);
                    }
                    if( type == 'SUBLOCALITY'){
                        district= getLongName(addressComponent);
                    }
                    if( type == 'ADMINISTRATIVE_AREA_LEVEL_1'){
                        area = getLongName(addressComponent);
                    }
                    if( type == 'COUNTRY'){
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
        var getAddressComponentOfTypeOrNull = function(addressComponents, targetType) {
            targetType = targetType || '';
            addressComponents = addressComponents || [];

            var len = addressComponents.length;
            for(var i = 0; i < len; i++) {
                var addressComponent = addressComponents[i];

                var types = addressComponent['types'] || [];

                var typeLen = types.length;
                for(var j = 0; j < typeLen; j++) {
                    var type = types[j].toUpperCase();
                    switch (targetType){
                        case 'COUNTRY': {
                            if("COUNTRY" == type) return getShortName(addressComponent);
                            break;
                        }
                        case 'COUNTRY_LONG': {
                            if("COUNTRY" == type) return getLongName(addressComponent);
                            break;
                        }
                        case 'CITY': {
                            return getCity(result);
                        }
                        case 'DISTRICT': {
                            if('SUBLOCALITY' == type){
                                return getLongName(addressComponent);
                            }

                            break;
                        }
                        case 'STREET_ADDRESS':
                        {
                            if('STREET_ADDRESS' == type){
                                return getLongName(addressComponent);
                            }

                            break;
                        }
                        case 'STREET_NUMBER':
                        {
                            if('STREET_NUMBER' == type){
                                return getLongName(addressComponent);
                            }

                            break;
                        }
                        case 'ROUTE':
                        {
                            if('ROUTE' == type){
                                return getLongName(addressComponent);
                            }

                            break;
                        }
                        case 'ROOM':
                        {
                            if('ROOM' == type){
                                return getLongName(addressComponent);
                            }

                            break;
                        }
                        case 'ZIP':
                        {
                            if('POSTAL_CODE' == type){
                                return getLongName(addressComponent);
                            }

                            break;
                        }
                        case 'STATE':{
                            if('ADMINISTRATIVE_AREA_LEVEL_1' == type) return getShortName(addressComponent);
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

        var buildAddress = function(addressComponents) {
            var addressBuilder = '';

            var streetAddress = getAddressComponentOfTypeOrNull(addressComponents, 'STREET_ADDRESS');
            var route = getAddressComponentOfTypeOrNull(addressComponents, 'ROUTE');
            var streetNumber = getAddressComponentOfTypeOrNull(addressComponents, 'STREET_NUMBER');
            var room = getAddressComponentOfTypeOrNull(addressComponents, 'ROOM');

            if(streetAddress) addressBuilder.append(streetAddress);
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

        var mapOptions = {
            center: { lat: -34.397, lng: 150.644},
            zoom: 16
        };

        var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

        var currentPlace = null;
        var marker = null;
        var autocomplete = new google.maps.places.Autocomplete(document.getElementById('addressInput'));
        google.maps.event.addListener(autocomplete, 'place_changed', function() {
            var place = autocomplete.getPlace();
            var addressComponents = place['address_components'];

            that.setState(
                {
                    data: jQuery.extend(that.state.data,
                        {
                            location:{
                                lat: place['geometry']['location'].lat(),
                                lng: place['geometry']['location'].lng()
                            },
                            address: {
                                city: getCity(addressComponents),
                                country: getAddressComponentOfTypeOrNull(addressComponents, 'COUNTRY_LONG'),
                                country_code: getAddressComponentOfTypeOrNull(addressComponents, 'COUNTRY'),
                                county: getAddressComponentOfTypeOrNull(addressComponents, 'STATE'),
                                district: getAddressComponentOfTypeOrNull(addressComponents, 'DISTRICT'),
                                street_address: buildAddress(addressComponents),
                                zip_code: getAddressComponentOfTypeOrNull(addressComponents, 'ZIP'),
                                formatted_address: place['formatted_address']
                            }
                        }
                    )
                }
            );
            currentPlace = {
                location: {
                    lat: place.geometry.location.lat(),
                    lng: place.geometry.location.lng()
                }
            };
            var latLng = new google.maps.LatLng(currentPlace.location.lat, currentPlace.location.lng);
            map.setCenter(latLng);
            if(marker != null) {
                marker.setMap(null);
            }
            marker = new google.maps.Marker({
                position: latLng,
                map: map,
                title: "Позиция на карте"
            });
        });

        var __base64Encode = function (data) {
            //  discuss at: http://phpjs.org/functions/base64_encode/
            // original by: Tyler Akins (http://rumkin.com)
            // improved by: Bayron Guevara
            // improved by: Thunder.m
            // improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
            // improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
            // improved by: Rafał Kukawski (http://kukawski.pl)
            // bugfixed by: Pellentesque Malesuada
            //   example 1: base64_encode('Kevin van Zonneveld');
            //   returns 1: 'S2V2aW4gdmFuIFpvbm5ldmVsZA=='
            //   example 2: base64_encode('a');
            //   returns 2: 'YQ=='
            //   example 3: base64_encode('✓ à la mode');
            //   returns 3: '4pyTIMOgIGxhIG1vZGU='

            var b64 = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
            var o1, o2, o3, h1, h2, h3, h4, bits, i = 0,
                ac = 0,
                enc = '',
                tmp_arr = [];

            if (!data) {
                return data;
            }

            data = unescape(encodeURIComponent(data));

            do {
                // pack three octets into four hexets
                o1 = data.charCodeAt(i++);
                o2 = data.charCodeAt(i++);
                o3 = data.charCodeAt(i++);

                bits = o1 << 16 | o2 << 8 | o3;

                h1 = bits >> 18 & 0x3f;
                h2 = bits >> 12 & 0x3f;
                h3 = bits >> 6 & 0x3f;
                h4 = bits & 0x3f;

                // use hexets to index into b64, and append result to encoded string
                tmp_arr[ac++] = b64.charAt(h1) + b64.charAt(h2) + b64.charAt(h3) + b64.charAt(h4);
            } while (i < data.length);

            enc = tmp_arr.join('');

            var r = data.length % 3;

            return (r ? enc.slice(0, r - 3) : enc) + '==='.slice(r || 3);
        };

        //TODO: un-hardcode it and move.
        var userNamePasswordHardCode = __base64Encode("user_dionis:eccafc45-2c2c-4028-931a-648975605899");

        var loadApartment = function() {
            $.ajax({
                url: '/rest/users/apartment',
                dataType: 'json',
                type: 'GET',
                beforeSend: function (request)
                {
                    request.setRequestHeader("Authorization", "Basic " + userNamePasswordHardCode);
                },
                success: function(data) {
                    that.setState({data: data});
                }.bind(that),
                error: function(xhr, status, err) {
                    console.error('/rest/apartments', status, err.toString());
                }.bind(that)
            });
        };

        var saveApartment = function() {
            $.ajax({
                url: '/rest/users/apartment',
                dataType: 'json',
                type: 'POST',
                beforeSend: function (request)
                {
                    request.setRequestHeader("Authorization", "Basic " + userNamePasswordHardCode);
                },
                success: function(data) {
//                    that.setState({data: data});
                }.bind(that),
                error: function(xhr, status, err) {
                    console.error('/rest/apartments', status, err.toString());
                }.bind(that)
            });
        };

        loadApartment();

        $('#saveApartmentBtn').on('click', function(){
            console.log('saving data:');
            console.log(that.state.data);
//            saveApartment();
        });
    },
    render: function () {
        var data = this.state.data || {};

        var addressProp = {id: 'addressInput', name: 'Адрес', placeholder: 'Начните печатать...', customClassName: 'col-md-8' };
        var rentTypeProp = {name: 'Тип сдачи',
            defaultDescription: 'Выберите тип сдачи',
            keyValuePairs: [
                ['LONG_TERM','Долгосрочная'],
                ['SHORT_TERM','Краткосрочная']
            ],
            customClassName: 'col-md-8',
            selectedValue: data?data['type_of_rent']:null
        };

        var rentalFeeProp = {name: 'Плата', customClassName: 'col-md-8', elementValue: data['rental_fee']};
        var feePeriodProp = {name: 'Интервал оплаты',
            defaultDescription: 'Минимальный период сдачи',
            keyValuePairs: [
                ['HOURLY','Час'],
                ['DAILY','День'],
                ['WEEKLY','Неделя'],
                ['MONTHLY','Месяц']
            ],
            customClassName: 'col-md-8',
            selectedValue: data['fee_period']
        };

        var roomCount = {name: 'Количество комнат', placeholder: '2', customClassName: 'col-md-8', elementValue: data['room_count']};
        var floorNumber = {name: 'Этаж', placeholder: '1', customClassName: 'col-md-8', elementValue: data['floor_number']};
        var floorsTotal = {name: 'Всего этажей', placeholder: '9', customClassName: 'col-md-8', elementValue: data['floors_total']};
        var area = {name: 'Площадь', placeholder: '42 м2', customClassName: 'col-md-8', elementValue: data['area']};

        var descriptionProp = {name: 'Описание', customClassName: 'col-md-8', elementValue: data['description']};

        var submitButton = {id: 'saveApartmentBtn', value: 'Сохранить', customClassName: 'col-md-4 col-md-offset-4'};

        var styles = {
            width: '300px',
            height: '300px'
        };

        return (
            <div className="col-md-9">
                <div className="panel">
                    <div className="panel-body">
                        <h4>Собственность</h4>

                        <br/>

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

var Settings = React.createClass({
    render: function () {
        var nameProp = {name: 'Имя'};
        var phoneProp = {name: 'Телефон'};
        var emailProp = {name: 'Email'};
        var roleProp = {name: 'Роль в системе'};

        var submitButton = {url: '#/user/submit', value: 'Сохранить'};

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>НАСТРОЙКИ</h4>

                        <br/>

                        <form className="form-horizontal" role="form">
                            <UserProperty data={nameProp}/>
                            <UserProperty data={phoneProp}/>
                            <UserProperty data={emailProp}/>
                            <UserProperty data={roleProp}/>

                            <UserButton data={submitButton}/>
                        </form>

                    </div>
                </div>
            </div>
        );
    }
});

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

        var optionNodes = keyValuePairs.map(function(pair){
            if(selectedValue == pair[0]) {
                return (
                    <option selected="selected" value={pair[0]}>{pair[1]}</option>
                    );
            } else {
                return (
                    <option value={pair[0]}>{pair[1]}</option>
                    );
            }
        });

        return (
            <div>
                <div className="form-group">
                    <label className="col-md-2 control-label">{this.props.data.name}:</label>
                    <div className={customClassName}>
                        <select id={this.props.data.id} className="form-control">
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