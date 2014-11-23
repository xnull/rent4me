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
    componentDidMount: function (rootNode) {
        var mapOptions = {
            center: { lat: -34.397, lng: 150.644},
            zoom: 18
        };

        var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);


        var currentPlace = null;
        var marker = null;
        var autocomplete = new google.maps.places.Autocomplete(document.getElementById('addressInput'));
        google.maps.event.addListener(autocomplete, 'place_changed', function() {
            var place = autocomplete.getPlace();
            console.log(place);
            console.log(place.geometry.location.lat());
            console.log(place.geometry.location.lng());
            currentPlace = {
                location: {
                    lat: place.geometry.location.lat(),
                    lng: place.geometry.location.lng()
                }
            };
            var latLng = new google.maps.LatLng(currentPlace.location.lat, currentPlace.location.lng)
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


        $('#saveApartmentBtn').on('click', function(){
           alert('saving');
        });
        //autocomplete.bindTo('bounds', map);
    },
    render: function () {

        var addressProp = {id: 'addressInput', name: 'Адрес', placeholder: 'Начните печатать...'};
        var rentTypeProp = {name: 'Тип сдачи',
            defaultDescription: 'Выберите тип сдачи',
            keyValuePairs: [
                ['LONG_TERM','Долгосрочная'],
                ['SHORT_TERM','Краткосрочная']
            ]
        };

        var rentalFeeProp = {name: 'Плата'};
        var feePeriodProp = {name: 'Интервал оплаты',
            defaultDescription: 'Минимальный период сдачи',
            keyValuePairs: [
                ['HOURLY','Час'],
                ['DAILY','День'],
                ['WEEKLY','Неделя'],
                ['MONTHLY','Месяц']
            ]
        };

        var roomCount = {name: 'Количество комнат', placeholder: '2'};
        var floorNumber = {name: 'Этаж', placeholder: '1'};
        var floorsTotal = {name: 'Всего этажей', placeholder: '9'};
        var area = {name: 'Площадь', placeholder: '42 м2'};

        var descriptionProp = {name: 'Описание'};

        var submitButton = {id: 'saveApartmentBtn', value: 'Сохранить'};

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
                        <div class="container-fluid">
                        <div class="row">
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
        return (
            <div>
                <div className="form-group">
                    <label className="col-md-2 control-label">{this.props.data.name}:</label>
                    <div className="col-md-4">
                        <input
                            id={this.props.data.id}
                            className="form-control"
                            type="text"
                            placeholder={this.props.data.placeholder}
                        />
                    </div>
                </div>
            </div>
        )
    }
});

var UserSelect = React.createClass({
    render: function () {
        var keyValuePairs = this.props.data.keyValuePairs || [];
        var description = this.props.data.defaultDescription || 'Выберите из списка';
        keyValuePairs.unshift(['', description]);

        var optionNodes = keyValuePairs.map(function(pair){
            return (
                    <option value={pair[0]}>{pair[1]}</option>
                );
        });

        return (
            <div>
                <div className="form-group">
                    <label className="col-md-2 control-label">{this.props.data.name}:</label>
                    <div className="col-md-4">
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
        return (
            <div>
                <div className="form-group">
                    <label className="col-md-2 control-label">{this.props.data.name}:</label>
                    <div className="col-md-4">
                        <textarea
                        id={this.props.data.id}
                        className="form-control"
                        rows="7"
                        placeholder={this.props.data.placeholder}
                        />
                    </div>
                </div>
            </div>
            )
    }
});

var UserButton = React.createClass({
    render: function () {
        return (
            <div className="col-md-2 col-md-offset-4">
                <a id={this.props.data.id} className="btn btn-primary center-block" href={this.props.data.url}>{this.props.data.value}</a>
            </div>
        )
    }
});