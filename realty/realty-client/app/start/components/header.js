/**
 * Created by null on 03.01.15.
 */

var React = require('react');
var AuthComponent = require('../../shared/components/socialNetAuth');
var SocialNetActions = require('../../shared/actions/SocialNetActions');
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

        var that = this;
        var autocomplete = new google.maps.places.Autocomplete(document.getElementById('addressInput'));

        google.maps.event.addListener(autocomplete, 'place_changed', function () {
            var place = autocomplete.getPlace();
            if (!place) return;
            var dump = JSON2.stringify(place);
            console.log('dump:');
            console.log(dump);
            var addressComponents = place['address_components'];

            var viewPort = place.geometry.viewport;

            var bounds = viewPort ? {
                northEast: {
                    lat: viewPort.getNorthEast().lat(),
                    lng: viewPort.getNorthEast().lng()
                },

                southWest: {
                    lat: viewPort.getSouthWest().lat(),
                    lng: viewPort.getSouthWest().lng()
                }
            } : null;

            var countryCode = AddressUtils.getAddressComponentOfTypeOrNull(addressComponents, 'COUNTRY');

            var location = {
                latitude: place['geometry']['location'].lat(),
                longitude: place['geometry']['location'].lng()
            };

            var formatted_address = place['name'];

            console.log('new location:');
            console.log(location);

            that.setState(assign(that.state, {
                location: location,
                countryCode: countryCode,
                bounds: bounds,
                formattedAddress: formatted_address
            }));

            that.rememberLocationInfo();

            mixpanel.track("Address_selected");
            MetrosActions.findMetros(location != null ? location.longitude : null, location != null ? location.latitude : null, countryCode, bounds);
        });

        MetrosStore.addChangeListener(this.onMetrosChanged);
        MetrosActions.findMetros(null, null, null, null);
        mixpanel.track("main_page_loaded");

    },

    rememberLocationInfo: function () {
        var location = this.state.location;
        var countryCode = this.state.countryCode;
        var bounds = this.state.bounds;
        var formattedAddress = this.state.formattedAddress;

        SocialNetActions.changeSearchLocationInfo(location, countryCode, bounds, formattedAddress);
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
    },

    changeToRenter: function () {
        console.log('changeToRenter');
        SocialNetActions.changeFBSearchType('RENTER');
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
            location: location,
            countryCode: null,
            bounds: null,
            formattedAddress: value
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

        var formattedAddress = this.state.formattedAddress;
        var tmpText = this.state.tmpText;
        var text = this.state.text;

        var metrosDisplayItem;

        {

            var _metros = this.state.metros;

            if (_.size(_metros) == 0) {
                //don't display
                metrosDisplayItem = null;
            } else {
                metrosDisplayItem = (<ReactAutocomplete
                    inputClassName="form-control"
                    placeholder="Выберите Метро Москвы"
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

        var sectionStyle = {
            //backgroundImage: "url('images/signin/22592__40P0163-01.jpg')",
            backgroundImage: "url('images/flats/flat3.jpg')",
            /*backgroundImage: "url('http://www.veskip.ru/images/property/22592__40P0163-01.jpg')",*/
            backgroundSize: 'cover',
            backgroundPosition: 'auto'
            /*backgroundRepeat: 'no-repeat'*/
            /*backgroundRepeat: 'round'*/
        };
        return (
            <div style={sectionStyle}>
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
                                                <RentType changeToRenter={this.changeToRenter}
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
                                                        <AddressBox displayValue={formattedAddress} onAddressChange={this.onAddressChange}/>
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