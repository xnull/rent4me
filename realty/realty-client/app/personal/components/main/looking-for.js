var React = require('react');
var ApartmentActions = require('../../../shared/actions/ApartmentActions');
var ApartmentStore = require('../../../shared/stores/ApartmentStore');
var AddressUtils = require('../../../shared/common/AddressUtils');
var Translations = require('../../../shared/l10n/Translations');

var assign = require('object-assign');
var _ = require('underscore');
var JSON2 = require('JSON2');

var moment = require('moment');
var accounting = require('accounting');

var AddressBox = React.createClass({
    render: function () {
        return (
            <input
                id="addressInput"
                className="form-control"
                type="text"
                placeholder="Введите город/район/метро"
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
        var hasMore = this.props.hasMore || false;

        console.log("Has more?" + hasMore);

        var onHasMoreClicked = this.props.onHasMoreClicked;

        var hasMoreElement = hasMore ?
            (
                <a href="javascript:void(0)" onClick={onHasMoreClicked} className="list-group-item">

                    <p className="list-group-item-text">
                        Загрузить еще
                    </p>

                </a>
            ) : null;

        var style = {};
        if (!shown) {
            style['display'] = 'none';
        }

        var newsItems = items.map(function (item) {
            return (
                <NewsItem item={item}/>
            );
        });

        return (
            <div className="col-md-9 col-sm-9 col-xs-9" style={style}>
                <div className="panel panel-default">
                    <div className="panel-heading">
                        <h4>Нововсти</h4>
                    </div>

                    <div className="panel-body">

                        <div className="bs-component">
                            <div className="list-group">
                            {newsItems}
                                <br/>
                            {hasMoreElement}
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

        var firstImage = _.first(item.photos.map(function (photo) {
            return (
                <img className="img-responsive" width="128"
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
            <div>
                <a href="#" className="list-group-item">
                    <h4 className="list-group-item-heading">{item.address ? item.address.formatted_address : 'no address'}</h4>
                        {imagePreviews}

                    <p className="list-group-item-text">
                        <div className="row">
                            <div className="col-md-6 col-sm-6 col-xs-6">
                                Этаж: {item.floor_number}/{item.floors_total}
                            </div>
                            <div className="col-md-6 col-sm-6 col-xs-6">
                                Площадь: {item.area} м
                                <sup>2</sup>
                            </div>
                        </div>

                        <br/>

                        <div className="row">
                            <div className="col-md-6 col-sm-6 col-xs-6">
                                Цена: {accounting.formatNumber(item.rental_fee, 0, " ")}/{Translations['ru'][item.fee_period]}
                            </div>
                            <div className="col-md-6 col-sm-6 col-xs-6">
                                Тип аренды: {Translations['ru'][item.type_of_rent]}
                            </div>

                        </div>

                        <br/>

                        <h4>Описание</h4>

                        <div className="row">
                            <div className="col-sm-12 col-md-12 col-xs-12">
                            {item.description}
                            </div>

                        </div>

                        <br/>

                        <h4>Контакты</h4>

                        <div className="row">
                            <div className="col-md-6 col-sm-6 col-xs-6">
                                Телефон: {item.owner.phone}
                            </div>
                        </div>

                        <hr/>

                        <div className="row">
                            <div className="col-md-6 col-sm-6 col-xs-6">
                                Добавлено: {moment(item.created).format("lll")}
                            </div>
                            <div className="col-md-6 col-sm-6 col-xs-6">
                                Личное сообщение: {moment(item.updated).format("lll")}
                            </div>
                        </div>

                    </p>
                    <hr/>
                </a>
                <br/>
            </div>
        );
    }
});

module.exports = React.createClass({
    getInitialState: function () {
        return {
            location: null,
            countryCode: null,
            bounds: null,
            apartments: ApartmentStore.getSearchResults(),
            hasMoreSearchResults: ApartmentStore.hasMoreSearchResults()
        };
    },

    componentWillUnmount: function () {
        ApartmentStore.removeChangeListener(this.onSearchResultsChanged);
    },

    onSearchResultsChanged: function () {
        console.log('on search results changed');
        var newSearchResults = ApartmentStore.getSearchResults();
        console.log(newSearchResults);
        this.setState(assign(this.state, {
            apartments: newSearchResults,
            hasMoreSearchResults: ApartmentStore.hasMoreSearchResults()
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
            if (!place) return;
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

        ApartmentStore.addChangeListener(this.onSearchResultsChanged);
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

    loadMoreResults: function () {
        if (this.state.hasMoreSearchResults) {
            var location = this.state.location;
            var countryCode = this.state.countryCode;
            var bounds = this.state.bounds;

            ApartmentActions.findNear(location.longitude, location.latitude, countryCode, bounds);
        }
    },

    render: function () {
        var items = this.state.apartments || [];
        var hasMoreResults = this.state.hasMoreSearchResults || false;

        return (
            <div className="col-md-9 col-sm-9 col-дп-9">
                <div className="panel">

                    <div className="panel-body">
                        <form className="form-horizontal" role="form">
                            <div className="form-group">
                                <label className="col-md-2 col-sm-2 col-xs-2 control-label">Адрес</label>
                                <div className="col-md-10 col-sm-10 col-xs-10">
                                    <AddressBox
                                        data=""
                                        onChange={this.handleAddressChange}
                                        ref = "addressInput"
                                    />
                                </div>
                            </div>

                            <div className="col-md-offset-9 col-sm-offset-9 col-xs-offset-9">
                                <a className="btn btn-primary center-block" onClick={this.onClick}>Поиск</a>
                            </div>
                        </form>
                    </div>
                </div>

                <News items={items} shown={items.length > 0} hasMore={hasMoreResults} onHasMoreClicked={this.loadMoreResults} />
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

