/**
 * Created by null on 03.01.15.
 */

var React = require('react');
var AuthComponent = require('../../shared/components/socialNetAuth');
var SocialNetActions = require('../../shared/actions/SocialNetActions');
var SocialNetStore = require('../../shared/stores/SocialNetStore');
var AppDispatcher = require('../../shared/dispatcher/AppDispatcher');
var Utils = require('rent4meUtil');
var Cookies = require('rent4meCookies');
var JSON2 = require('JSON2');
var assign = require('object-assign');
var ReactAutocomplete = require('rent4meAutocomplete');

var RoomsCount = require('../../shared/ui/rooms-count');
var PriceRange = require('../../shared/ui/price-range');
var RentType = require('../../shared/ui/rent-type');
var AddressBox = require('../../shared/ui/search-address2');

var MetroPopover = require('../../shared/ui/metro-popover');
var SearchWithTextPopover = require('../../shared/ui/search-with-text-popover');
var SearchTermBubble = require('../../shared/ui/search-term-bubble');

var MetrosStore = require('../../shared/stores/MetrosStore');
var MetrosActions = require('../../shared/actions/MetrosActions');

var UserLocationStore = require('../../shared/stores/user-location-store');
var UserLocationActions = require('../../shared/actions/UserLocationActions');

var CityStore = require('../../shared/stores/city-store');
var CityActions = require('../../shared/actions/city-actions');

var AddressUtils = require('../../shared/common/AddressUtils');

var _ = require('underscore');

var Cards = require('./cards');

var HeaderComponent = React.createClass({

    getInitialState: function () {
        return {
            oneRoomAptSelected: false,
            twoRoomAptSelected: false,
            threeRoomAptSelected: false,
            minPrice: null,
            maxPrice: null,
            tmpText: null,
            text: null,
            tmpMetroSelected: null,
            metros: MetrosStore.getMetros(),
            metrosSelected: []
        }
    },

    componentDidMount: function () {
        SocialNetActions.resetFBSearchState();

        SocialNetStore.addChangeLocationListener(this.onAddressLocationChangedCallback);

        MetrosStore.addChangeListener(this.onMetrosChanged);

        CityStore.addChangeListener(this.onCityChanged);

        UserLocationStore.addChangeListener(this.onUserLocationChanged);
        UserLocationActions.retrieveUserLocation();

    },

    onAddressLocationChangedCallback: function() {
        this.setState(assign(this.state, {
            location: SocialNetStore.getLocation(),
            countryCode: SocialNetStore.getCountryCode(),
            bounds: SocialNetStore.getBounds(),
            formattedAddress: SocialNetStore.getFormattedAddress(),
            formattedName: SocialNetStore.getFormattedName()
        }));
    },

    onCityChanged: function() {
        console.log('on city changed');

        var city = CityStore.getCurrentCity();

        console.log(city);

        var self = this;

        if(city != null) {
            var bounds = {
                northEast: {
                    lat: city.area.high.latitude,
                    lng: city.area.high.longitude
                },

                southWest: {
                    lat: city.area.low.latitude,
                    lng: city.area.low.longitude
                }
            };

            var obj = {
                location: city.center_point,
                bounds: bounds,
                formattedAddress: city.name,
                formattedName: city.name
            };
        }
    },

    onUserLocationChanged: function () {
        console.log('on location changed');
        var location = UserLocationStore.getLocation();

        CityActions.retrieveUserCity(location ? location.lng : null, location ? location.lat : null);
    },

    onAddressSelected: function(value) {
        console.log("Received value on selection: "+JSON2.stringify(value));


        this.setState(assign(this.state, {
            location: value.location,
            countryCode: value.countryCode,
            bounds: value.bounds,
            formattedAddress: value.formattedAddress,
            formattedName: value.formattedName
        }));

        this.rememberLocationInfo();
        MetrosActions.findMetros(value.location != null ? value.location.longitude : null,
            value.location != null ? value.location.latitude : null,
            value.countryCode,
            value.bounds);
    },

    rememberLocationInfo: function () {
        var location = this.state.location;
        var countryCode = this.state.countryCode;
        var bounds = this.state.bounds;
        var formattedName = this.state.formattedName;
        var formattedAddress = this.state.formattedAddress;

        SocialNetActions.changeSearchLocationInfo(location, countryCode, bounds, formattedAddress, formattedName);
    },

    componentWillUnmount: function () {
        MetrosStore.removeChangeListener(this.onMetrosChanged);
    },

    onMetrosChanged: function () {
        console.log('on search results changed');
        var newSearchResults = MetrosStore.getMetros();
        console.log(newSearchResults);
        this.setState(assign(this.state, {
            metros: newSearchResults
        }));
    },

    onOneRoomAptValueChanged: function (value) {
        console.log('onOneRoomAptValueChanged to: ' + value);

        this.setState(assign(this.state, {
            oneRoomAptSelected: value
        }));

        this.fireAptSelectionStateChange();
    },

    onTwoRoomAptValueChanged: function (value) {
        this.setState(assign(this.state, {
            twoRoomAptSelected: value
        }));

        this.fireAptSelectionStateChange();
    },

    onThreeRoomAptValueChanged: function (value) {
        this.setState(assign(this.state, {
            threeRoomAptSelected: value
        }));

        this.fireAptSelectionStateChange();
    },

    fireAptSelectionStateChange: function () {
        console.log('fireAptSelectionStateChange');

        var oneRoomAptSelected = this.state.oneRoomAptSelected;
        var twoRoomAptSelected = this.state.twoRoomAptSelected;
        var threeRoomAptSelected = this.state.threeRoomAptSelected;

        SocialNetActions.changeFBSearchRooms(oneRoomAptSelected, twoRoomAptSelected, threeRoomAptSelected);
    },

    performSearch: function () {
        Utils.navigateToPersonal();
    },

    performSearchOnEnter: function (e) {
        if (e.key == 'Enter') {
            this.performSearch();
        }
    },

    changeToLessor: function () {
        console.log('changeToLessor');
        SocialNetActions.changeFBSearchType('LESSOR');
        this.setState(assign(this.state, {
            type: 'LESSOR'
        }));
    },

    changeToRenter: function () {
        console.log('changeToRenter');
        SocialNetActions.changeFBSearchType('RENTER');
        this.setState(assign(this.state, {
            type: 'RENTER'
        }));
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

    onTmpSearchChange: function (e) {
        var value = e.target.value;

        this.setState(assign(this.state, {
            tmpText: value
        }));
    },

    onSearchChange: function () {
        var tmpText = this.state.tmpText;

        if (!tmpText) return;

        console.log('on search change');

        SocialNetActions.changeFBSearchText(tmpText);

        this.setState(assign(this.state, {
            text: tmpText,
            tmpText: null
        }));
    },

    onRemoveTextTag: function (ignored) {
        SocialNetActions.changeFBSearchText(null);

        this.setState(assign(this.state, {
            text: null
        }));
    },

    onAddressChange: function (event) {
        this.makeFormattedAddressDirty(event.target.value);
    },

    makeFormattedAddressDirty: function (value) {
        this.setState(assign(this.state, {
            location: null,
            countryCode: null,
            bounds: null,
            formattedName: value,
            formattedAddress: null
        }));
    },

    _searchRemote: function (options, searchTerm, cb) {
        var metros = this.state.metros;

        var transformedMetros = metros.map(m => {
            return {id: m.id, title: m.station_name}
        });
        console.log('metro_typehead: ');

        cb(null, transformedMetros.filter(m=> {
            return (m.title || '').toLowerCase().indexOf((searchTerm || '').toLowerCase()) === 0;
        }));
    },

    onTargetMetroSelected: function () {
        var item = this.state.tmpMetroSelected;

        if (!item) return;

        var metrosSelected = []
            .concat(this.state.metrosSelected)
            .filter(i=>i.id != item.id);
        metrosSelected.push(item);
        this.setState(assign(this.state, {
            metrosSelected: metrosSelected,
            tmpMetroSelected: null
        }));
        this.fireMetrosSelectedChange();
    },

    onTempMetroSelected: function (item) {
        this.setState(assign(this.state, {
            tmpMetroSelected: item
        }));
    },

    onRemoveMetroTag: function (itemId) {
        var metrosSelected = []
            .concat(this.state.metrosSelected)
            .filter(i=>i.id != itemId);
        this.setState(assign(this.state, {
            metrosSelected: metrosSelected
        }));
        this.fireMetrosSelectedChange();
    },

    fireMetrosSelectedChange: function () {
        var metrosSelected = this.state.metrosSelected;

        SocialNetActions.changeSearchMetros(metrosSelected);
    },

    render: function () {

        var loginButtonStyle = {
            margin: 30,
            /*background: 'rgba(74, 35, 23, 0.4)'*/
            background: 'rgba(3, 3, 3, 0.45)'
        };


        var authComponentDisplayItem = (<input type="button" className="button special" value="Вход / Регистрация" style={loginButtonStyle}/>);

        var tmpText = this.state.tmpText;
        var text = this.state.text;

        var metrosDisplayItem = null;

        {

            var _metros = this.state.metros;

            if (_.size(_metros) == 0) {
                //don't display
                metrosDisplayItem = null;
            } else {
                metrosDisplayItem = (<ReactAutocomplete
                    inputClassName="form-control"
                    placeholder="Выберите Метро"
                    search={this._searchRemote}
                    onChange={this.onTempMetroSelected}/>);
            }
        }

        var bubbles = null;


        var _metrosSelected = this.state.metrosSelected;
        if (_.size(_metrosSelected) > 0 || text) {
            var _bubbles = _metrosSelected.map(m => {
                return (<SearchTermBubble id={m.id} displayValue={"Метро: " + m.title} onRemove={this.onRemoveMetroTag}/>);
            });

            if (text) {
                _bubbles.push(
                    <SearchTermBubble id={-1} displayValue={"текст: " + text} onRemove={this.onRemoveTextTag}/>
                );
            }

            bubbles = (
                <div className='row'>
                    <div className="col-md-12 col-sm-12 col-xs-12 pull-left" style={{paddingTop: 0}}>
                        {_bubbles}
                    </div>
                </div>
            );
        }

        var addressInitialValue = {
            location: this.state.location,
            countryCode: this.state.countryCode,
            bounds: this.state.bounds,
            formattedAddress: this.state.formattedAddress,
            formattedName: this.state.formattedName
        };

        console.log('Address initial value');
        console.log(addressInitialValue);

        return (
            <div className='header-cover'>
                <div>
                    <div style={{textAlign: 'right'}}>
                        <AuthComponent displayItem={authComponentDisplayItem}/>
                    </div>
                </div>
                <section id='header' className='dark'>
                    <header>
                        <h1 style={{color: '#da5037'}}>Аренда недвижимости</h1>
                        <p>
                            <strong style={{color: 'rgb(168, 129, 66)'}}>Прозрачность, отсутствие посредников</strong>
                        </p>
                    </header>
                    <footer>
                        <div className="row">
                            <div className='col-xs-12 col-sm-12 col-md-10 col-lg-7 col-centered'>
                                <div className="panel">
                                    <div className="panel-body">
                                        <form className="form" role="form">

                                            <div className='row'>
                                                <RentType
                                                    changeToRenter={this.changeToRenter}
                                                    changeToLessor={this.changeToLessor}
                                                    className="col-xs-6 col-sm-6 col-md-4"
                                                />

                                                <RoomsCount
                                                    className="col-xs-6 col-sm-6 col-md-4"

                                                    oneRoomAptSelected={this.state.oneRoomAptSelected}
                                                    twoRoomAptSelected={this.state.twoRoomAptSelected}
                                                    threeRoomAptSelected={this.state.threeRoomAptSelected}

                                                    onOneRoomAptValueChanged={this.onOneRoomAptValueChanged}
                                                    onTwoRoomAptValueChanged={this.onTwoRoomAptValueChanged}
                                                    onThreeRoomAptValueChanged={this.onThreeRoomAptValueChanged}
                                                />

                                                <PriceRange
                                                    className="col-xs-12 col-sm-12 col-md-4"

                                                    onKeyPress={this.performSearchOnEnter}

                                                    minPrice={this.state.minPrice}
                                                    maxPrice={this.state.maxPrice}

                                                    onMinPriceChange={this.onMinPriceChange}
                                                    onMaxPriceChange={this.onMaxPriceChange}
                                                />

                                            </div>

                                            <br/>

                                            <div className='row'>
                                                <div className="col-md-8 col-sm-12 col-xs-12">
                                                    <div className="input-group">
                                                        <AddressBox
                                                            onAddressChange={this.onAddressChange}
                                                            onAddressSelected={this.onAddressSelected}
                                                            initialValue={addressInitialValue}
                                                        />
                                                        <div className="input-group-btn">
                                                            <MetroPopover
                                                                metroInput={metrosDisplayItem}
                                                                addButtonEnabled={this.state.tmpMetroSelected != null}
                                                                onAddButtonClicked={this.onTargetMetroSelected}
                                                            />
                                                        </div>
                                                        <div className="input-group-btn">
                                                            <SearchWithTextPopover textInput={(
                                                                <input type="text" className="form-control" value={tmpText}
                                                                    placeholder="Поиск по тексту объявления"
                                                                    onKeyPress={this.clickOnEnter}
                                                                    onChange={this.onTmpSearchChange} >
                                                                </input>)}
                                                                addButtonEnabled={tmpText != null}
                                                                onAddButtonClicked={this.onSearchChange}
                                                            />
                                                        </div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4 col-sm-12 col-xs-12">
                                                    <a className="btn btn-success center-block" onClick={this.performSearch}>Найти</a>
                                                </div>
                                            </div>
                                            {bubbles}
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </footer>
                </section>
            </div>
        )
    }
});

module.exports = HeaderComponent;