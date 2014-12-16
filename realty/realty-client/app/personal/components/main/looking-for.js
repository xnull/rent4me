var React = require('react');
var ApartmentActions = require('../../../shared/actions/ApartmentActions');
var ApartmentStore = require('../../../shared/stores/ApartmentStore');
var AddressUtils = require('../../../shared/common/AddressUtils');

var assign = require('object-assign');
var _ = require('underscore');
var JSON2 = require('JSON2');


var AddressBox = React.createClass({
    render: function () {
        return (
            <input
                id="addressInput"
                className="form-control"
                type="text"
                placeholder="Введите местоположение"
                onChange={this.onAddressChange}
            />
        )
    },

    onAddressChange: function (event) {
        //this.props.onChange(event.target.value);
    }
});

var News = React.createClass({

    render: function () {
        var shown = this.props.shown || false;
        var items = this.props.items || [];

        console.log('shown?');
        console.log(shown);

        var style = {};
        if(!shown) {
            style['display'] = 'none';
        }

        var newsItems = items.map(function (item) {
            return (
                <NewsItem item={item}/>
            );
        });

        return (
            <div className="col-md-9" style={style}>
                <div className="panel panel-default">
                    <div className="panel-heading">
                        <h4>Нововсти</h4>
                    </div>

                    <div className="panel-body">

                        <div className="bs-component">
                            <div className="list-group">

                            {newsItems}

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

var NewsItem = React.createClass({
    render: function () {
        var item = this.props.item || {};

        var firstImage = _.first(item.photos.map(function(photo) {
            return (
                <img className="img-responsive"
                    src={photo.small_thumbnail_url}/>
            );
        }));

        var imagePreviews = firstImage ? (
            <div>
            <div className="panel-thumbnail">
                {firstImage}
            </div>

            <hr/>
            </div>
        ) : null;

        return (
            <a href="#" className="list-group-item">
                <h4 className="list-group-item-heading">{item.address ? item.address.formatted_address : 'no address'}</h4>

                {imagePreviews}

                <p className="list-group-item-text">
                    <div className="row">
                        <div className="col-md-6">
                            Этаж: {item.floor_number}/{item.floors_total}
                        </div>
                        <div className="col-md-6">
                            Площадь: {item.area} м<sup>2</sup>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-6">
                            Цена: {item.rental_fee}/{item.fee_period}
                        </div>
                        <div className="col-md-6">
                            Тип аренды: {item.type_of_rent}
                        </div>
                    </div>

                </p>
                <hr/>

                <br/>


            </a>
        );
    }
});

module.exports = React.createClass({
    getInitialState: function () {
        return {
            location: null,
            countryCode: null,
            bounds: null,
            apartments: ApartmentStore.getSearchResults()
        };
    },

    componentWillMount: function () {
        ApartmentStore.addChangeListener(this.onSearchResultsChanged);
    },

    componentWillUnmount: function () {
        ApartmentStore.removeChangeListener(this.onSearchResultsChanged);
    },

    onSearchResultsChanged: function () {
        console.log('on search results changed');
        var newSearchResults = ApartmentStore.getSearchResults();
        console.log(newSearchResults);
        this.setState(assign(this.state, {
            apartments: newSearchResults
        }));
    },

    /**
     * https://developers.google.com/places/?hl=ru
     * https://developers.google.com/maps/documentation/javascript/examples/places-autocomplete?hl=ru
     *
     * @param rootNode
     */
    componentDidMount: function (rootNode) {
        var that = this;
        var autocomplete = new google.maps.places.Autocomplete(document.getElementById('addressInput'));

        google.maps.event.addListener(autocomplete, 'place_changed', function () {
            var place = autocomplete.getPlace();
            var dump = JSON2.stringify(place);
            console.log('dump:');
            console.log(dump);
            var addressComponents = place['address_components'];

            var bounds = place.geometry.viewport;

            //console.log("Bounds:");
            //console.log(JSON2.stringify(bounds));
            //console.log("NE:");
            //console.log(bounds.getNorthEast());
            //console.log("SW:");
            //console.log(bounds.getSouthWest());

            var countryCode = AddressUtils.getAddressComponentOfTypeOrNull(addressComponents, 'COUNTRY');

            var location = {
                latitude: place['geometry']['location'].lat(),
                longitude: place['geometry']['location'].lng()
            };

            console.log('new location:');
            console.log(location);

            that.setState(assign(that.state, {location: location, countryCode: countryCode, bounds: bounds}));
            that.onSearchStart();
        });
    },

    onSearchStart: function () {
        var location = this.state.location;
        var countryCode = this.state.countryCode;
        var bounds = this.state.bounds;
        console.log('On search start');
        console.log(location);
        if (!location) {
            alert('Пожалуйста, введите адрес');
        } else {
            ApartmentActions.resetSearchState();
            ApartmentActions.findNear(location.longitude, location.latitude, countryCode, bounds);
        }
    },

    render: function () {
        var items = this.state.apartments || [];

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Мне интересно</h4>


                        <form className="form-horizontal" role="form">
                            <div className="form-group">
                                <label className="col-md-2 control-label">Адрес</label>
                                <div className="col-md-10">
                                    <AddressBox
                                        data=""
                                        onChange={this.handleAddressChange}
                                        ref = "addressInput"
                                    />
                                </div>
                            </div>

                            <div className="col-md-offset-9">
                                <a className="btn btn-primary center-block" onClick={this.onClick}>Поиск</a>
                            </div>
                        </form>
                    </div>
                </div>

                <News items={items} shown={items.length > 0}/>
            </div>
        );
    },

    handleAddressChange: function (address) {
        console.log(address);
        //this.setState({
        //    data: {address: address}
        //});
    },

    onClick: function () {
        this.onSearchStart();
    }
});

