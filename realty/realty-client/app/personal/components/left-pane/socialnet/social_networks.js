/**
 * Created by dionis on 04/01/15.
 */
var React = require('react');
var Util = require('rent4meUtil');
var assign = require('object-assign');

var SocialNetStore = require('../../../../shared/stores/SocialNetStore');
var SocialNetActions = require('../../../../shared/actions/SocialNetActions');

var RoomsCount = require('../../../../shared/ui/rooms-count');
var PriceRange = require('../../../../shared/ui/price-range');
var RentType = require('../../../../shared/ui/rent-type');

var AddressUtils = require('../../../../shared/common/AddressUtils');
var JSON2 = require('JSON2');

var _ = require('underscore');
var moment = require('moment');
var Posts = require('./posts');

var AddressBox = React.createClass({
    render: function () {
        return (
            <input
                id="addressInput"
                className="form-control"
                type="text"
                value={this.props.displayValue}
                placeholder="Введите местоположение"
                onChange={this.onAddressChange}
            />
        )
    },

    onAddressChange: function (event) {
        //this.props.onChange(event.target.value);
    }
});

module.exports = React.createClass({
    getInitialState: function () {
        return {
            withSubway: SocialNetStore.isSearchWithSubway(),
            text: SocialNetStore.getSearchText(),
            type: SocialNetStore.getSearchType(),
            posts: SocialNetStore.getSearchResults(),
            oneRoomAptSelected: SocialNetStore.getSearchRooms()["1"],
            twoRoomAptSelected: SocialNetStore.getSearchRooms()["2"],
            threeRoomAptSelected: SocialNetStore.getSearchRooms()["3"],
            location: SocialNetStore.getLocation(),
            countryCode: SocialNetStore.getCountryCode(),
            bounds: SocialNetStore.getBounds(),
            formattedAddress: SocialNetStore.getFormattedAddress(),
            minPrice: SocialNetStore.getSearchMinPrice(),
            maxPrice: SocialNetStore.getSearchMaxPrice(),
            hasMoreSearchResults: SocialNetStore.hasMoreSearchResults(),
            lastSearchTextChangeMS: 0,
            lastSearchMinPriceChangeMS: 0,
            lastSearchMaxPriceChangeMS: 0
        };
    },

    componentDidMount: function () {
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

            var countryCode = AddressUtils.getAddressComponentOfTypeOrNull(addressComponents, 'COUNTRY');

            var location = {
                latitude: place['geometry']['location'].lat(),
                longitude: place['geometry']['location'].lng()
            };

            var formatted_address = place['formatted_address'];

            console.log('new location:');
            console.log(location);

            that.setState(assign(that.state, {location: location, countryCode: countryCode, bounds: bounds, formattedAddress: formatted_address}));
            that.onClick();
        });

        SocialNetStore.addChangeListener(this.onSearchResultsChanged);
        this.onClick();//trigger initial load
    },

    componentWillUnmount: function () {
        SocialNetStore.removeChangeListener(this.onSearchResultsChanged);
    },

    onSearchResultsChanged: function () {
        console.log('on search results changed');
        var newSearchResults = SocialNetStore.getSearchResults();
        console.log(newSearchResults);
        this.setState(assign(this.state, {
            posts: newSearchResults,
            hasMoreSearchResults: SocialNetStore.hasMoreSearchResults()
        }));
    },

    loadMoreResults: function () {
        if (this.state.hasMoreSearchResults) {
            var text = this.state.text;
            var withSubway = true;
            var type = this.state.type;

            var oneRoomAptSelected = this.state.oneRoomAptSelected;
            var twoRoomAptSelected = this.state.twoRoomAptSelected;
            var threeRoomAptSelected = this.state.threeRoomAptSelected;

            var minPrice = this.state.minPrice;
            var maxPrice = this.state.maxPrice;

            var location = this.state.location;
            var countryCode = this.state.countryCode;
            var bounds = this.state.bounds;
            var formattedAddress = this.state.formattedAddress;

            console.log('Searching for text: ' + text);
            SocialNetActions.findPosts(text, type, withSubway, oneRoomAptSelected, twoRoomAptSelected, threeRoomAptSelected, minPrice, maxPrice, location != null ? location.longitude : null, location != null ? location.latitude : null, countryCode, bounds, formattedAddress);
        }
    },

    onSearchChange: function (e) {
        var value = e.target.value;

        console.log('on search change');

        SocialNetActions.changeFBSearchText(value);

        this.setState(assign(this.state, {
            text: value
        }));
    },

    onRentTypeChange: function (value) {
        console.log("With subway new value: " + value);

        SocialNetActions.changeFBSearchType(value);

        this.setState(assign(this.state, {
            type: value
        }));

        this.onClick();
    },

    onMinPriceChange: function (e) {
        var value = e.target.value;
        console.log("With min price new value: " + value);

        var minPrice = value;
        var maxPrice = this.state.maxPrice;

        SocialNetActions.changeFBSearchPrice(minPrice, maxPrice);

        this.setState(assign(this.state, {
            minPrice: minPrice
        }));
    },

    onMaxPriceChange: function (e) {
        var value = e.target.value;
        console.log("With max price new value: " + value);

        var minPrice = this.state.minPrice;
        var maxPrice = value;

        SocialNetActions.changeFBSearchPrice(minPrice, maxPrice);

        this.setState(assign(this.state, {
            maxPrice: maxPrice
        }));
    },

    fireAptSelectionStateChange: function () {
        var oneRoomAptSelected = this.state.oneRoomAptSelected;
        var twoRoomAptSelected = this.state.twoRoomAptSelected;
        var threeRoomAptSelected = this.state.threeRoomAptSelected;

        SocialNetActions.changeFBSearchRooms(oneRoomAptSelected, twoRoomAptSelected, threeRoomAptSelected);
    },

    onOneRoomAptValueChanged: function (value) {
        this.setState(assign(this.state, {
            oneRoomAptSelected: value
        }));

        this.fireAptSelectionStateChange();

        this.onClick();
    },

    onTwoRoomAptValueChanged: function (value) {
        this.setState(assign(this.state, {
            twoRoomAptSelected: value
        }));

        this.fireAptSelectionStateChange();

        this.onClick();
    },

    onThreeRoomAptValueChanged: function (value) {
        this.setState(assign(this.state, {
            threeRoomAptSelected: value
        }));

        this.fireAptSelectionStateChange();

        this.onClick();
    },

    onClear: function () {
        SocialNetActions.resetFBSearchState();
        SocialNetActions.changeFBSearchText(null);
        SocialNetActions.changeFBSearchType(null);
        SocialNetActions.changeFBSearchWithSubway(true);
        SocialNetActions.changeFBSearchRooms(false, false, false);
        SocialNetActions.changeFBSearchPrice(null, null);
        SocialNetActions.changeFBSearchPrice(null, null, null, null);

        this.setState(assign(this.state, {
            withSubway: SocialNetStore.isSearchWithSubway(),
            type: SocialNetStore.getSearchType(),
            text: SocialNetStore.getSearchText(),
            oneRoomAptSelected: SocialNetStore.getSearchRooms()["1"],
            twoRoomAptSelected: SocialNetStore.getSearchRooms()["2"],
            threeRoomAptSelected: SocialNetStore.getSearchRooms()["3"],
            location: SocialNetStore.getLocation(),
            countryCode: SocialNetStore.getCountryCode(),
            bounds: SocialNetStore.getBounds(),
            formattedAddress: SocialNetStore.getFormattedAddress()
        }));
    },

    clickOnEnter: function(e) {
        if(e.key=='Enter') {
            this.onClick();
        }
    },

    onClick: function () {
        var text = this.state.text;
        var withSubway = true;
        var type = this.state.type;
        var oneRoomAptSelected = this.state.oneRoomAptSelected;
        var twoRoomAptSelected = this.state.twoRoomAptSelected;
        var threeRoomAptSelected = this.state.threeRoomAptSelected;

        var minPrice = this.state.minPrice;
        var maxPrice = this.state.maxPrice;

        var location = this.state.location;
        var countryCode = this.state.countryCode;
        var bounds = this.state.bounds;
        var formattedAddress = this.state.formattedAddress;

        console.log('Searching for text: ' + text + "& min price: " + minPrice + " & max price " + maxPrice);

        SocialNetActions.resetFBSearchState();
        SocialNetActions.changeFBSearchText(text);
        SocialNetActions.changeFBSearchWithSubway(withSubway);
        SocialNetActions.changeFBSearchType(type);
        this.fireAptSelectionStateChange();
        SocialNetActions.changeFBSearchPrice(minPrice, maxPrice);
        SocialNetActions.changeSearchLocationInfo(location, countryCode, bounds, formattedAddress);
        SocialNetActions.findPosts(text, type, withSubway, oneRoomAptSelected, twoRoomAptSelected, threeRoomAptSelected, minPrice, maxPrice, location != null ? location.longitude : null, location != null ? location.latitude : null, countryCode, bounds, formattedAddress);
    },

    changeToLessor: function () {
        console.log('changeToLessor');
        this.onRentTypeChange('LESSOR');
    },

    changeToRenter: function () {
        console.log('changeToRenter');
        this.onRentTypeChange('RENTER');
    },

    render: function () {
        var items = this.state.posts || [];
        var hasMoreResults = this.state.hasMoreSearchResults || false;
        var text = this.state.text || '';
        var withSubWay = true;

        var oneRoomAptSelected = this.state.oneRoomAptSelected;
        var twoRoomAptSelected = this.state.twoRoomAptSelected;
        var threeRoomAptSelected = this.state.threeRoomAptSelected;

        var minPrice = this.state.minPrice;
        var maxPrice = this.state.maxPrice;

        var formattedAddress = this.state.formattedAddress;

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Мне интересно</h4>

                        <form className="form-horizontal" role="form">
                            <div className='row'>
                                <div className="col-md-10 col-md-offset-1">
                                    <RentType changeToRenter={this.changeToRenter} changeToLessor={this.changeToLessor}/>
                                    <RoomsCount
                                        uiSize='4' uiLabelSize='3'

                                        oneRoomAptSelected={oneRoomAptSelected}
                                        twoRoomAptSelected={twoRoomAptSelected}
                                        threeRoomAptSelected={threeRoomAptSelected}

                                        onOneRoomAptValueChanged={this.onOneRoomAptValueChanged}
                                        onTwoRoomAptValueChanged={this.onTwoRoomAptValueChanged}
                                        onThreeRoomAptValueChanged={this.onThreeRoomAptValueChanged}
                                    />

                                    <PriceRange
                                        uiSize='5' uiLabelSize='2'

                                        minPrice={minPrice}
                                        maxPrice={maxPrice}

                                        onKeyPress={this.clickOnEnter}

                                        onMinPriceChange={this.onMinPriceChange}
                                        onMaxPriceChange={this.onMaxPriceChange}
                                    />
                                </div>
                            </div>

                            <div className='row'>
                                <div className="col-md-8 col-md-offset-1">
                                    <div className="col-md-12">
                                        <AddressBox displayValue={formattedAddress}/>
                                    </div>
                                </div>
                                <div className="col-md-2 pull-left">
                                    <select
                                        className="form-control"
                                        multiple={false}
                                        >
                                        <option key="" value="">Выберите метро</option>
                                        <option key="metro1" value="metro1">Метро1</option>
                                        <option key="metro2" value="metro2">Метро2</option>
                                        <option key="metro3" value="metro3">Метро3</option>
                                        <option key="metro4" value="metro4">Метро4</option>
                                        <option key="metro5" value="metro5">Метро5</option>
                                    </select>
                                </div>
                            </div>

                            <br/>

                            <div className='row'>
                                <div className="col-md-8 col-md-offset-1">
                                    <div className="col-md-12">
                                        <input type="text" className="form-control" value={text}
                                            placeholder="Поиск по тексту объявления"
                                            onKeyPress={this.clickOnEnter}
                                            onChange={this.onSearchChange} >
                                        </input>
                                    </div>
                                </div>
                                <div className="col-md-2">
                                    <a className="btn btn-primary" onClick={this.onClick}>Найти</a>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>

                <Posts items={items} shown={items.length > 0} hasMore={hasMoreResults} onHasMoreClicked={this.loadMoreResults} />
            </div>
        );
    }
});