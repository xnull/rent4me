/**
 * Created by dionis on 04/01/15.
 */
var React = require('react');
var Util = require('rent4meUtil');
var Ajax = require('rent4meAjax');
var assign = require('object-assign');

var SocialNetStore = require('../../../../shared/stores/SocialNetStore');
var SocialNetActions = require('../../../../shared/actions/SocialNetActions');

var RoomsCount = require('../../../../shared/ui/rooms-count');
var PriceRange = require('../../../../shared/ui/price-range');
var RentType = require('../../../../shared/ui/rent-type');
var SearchTermBubble = require('../../../../shared/ui/search-term-bubble');

var AddressUtils = require('../../../../shared/common/AddressUtils');
var JSON2 = require('JSON2');

var _ = require('underscore');
var moment = require('moment');
var Posts = require('./posts');
var NavActions = require('../../../../shared/actions/NavActions');
var MetrosActions = require('../../../../shared/actions/MetrosActions');
var MetrosStore = require('../../../../shared/stores/MetrosStore');

var ReactAutocomplete = require('rent4meAutocomplete');

var ButtonToolbar = require('react-bootstrap/ButtonToolbar');
var Popover = require('react-bootstrap/Popover');
var OverlayTrigger = require('react-bootstrap/OverlayTrigger');
var Button = require('react-bootstrap/Button');

var AddressBox = React.createClass({

    render: function () {
        return (
            <input
                id="addressInput"
                className="form-control"
                type="text"
                value={this.props.displayValue}
                placeholder="Введите местоположение"
                onChange={this.props.onAddressChange}
            />
        )
    }
});

var MetroPopover = React.createClass({

    render: function () {

        var enabled = this.props.addButtonEnabled;

        var style = {};

        if (!enabled) {
            style = Util.inactiveUi;
        }

        return (
            <ButtonToolbar>
                <OverlayTrigger trigger="click" placement="bottom" overlay={
                    <Popover title="Поиск по станциям метро">
                        {this.props.metroInput}
                        <button type="button" className="btn btn-default btn-block" onClick={this.props.onAddButtonClicked} style={style}>
                            Добавить
                        </button>
                    </Popover>
                    }
                >
                    <Button bsStyle="default">Метро</Button>
                </OverlayTrigger>
            </ButtonToolbar>
        )
    }
});

var SearchWithTextPopover = React.createClass({

    render: function () {

        var addButtonEnabled = this.props.addButtonEnabled;
        var onAddButtonClicked = this.props.onAddButtonClicked;

        var style = {};

        if (!addButtonEnabled) {
            style = Util.inactiveUi;
        }

        return (
            <ButtonToolbar>
                <OverlayTrigger trigger="click" placement="bottom" overlay={
                    <Popover title="Поиск по тексту в объявлениях">
                        {this.props.textInput}
                        <button type="button" className="btn btn-default btn-block" onClick={onAddButtonClicked} style={style}>
                            Добавить
                        </button>
                    </Popover>
                    }
                >
                    <Button bsStyle="default">Текст</Button>
                </OverlayTrigger>
            </ButtonToolbar>
        )
    }
});

module.exports = React.createClass({
    getInitialState: function () {
        return {
            withSubway: SocialNetStore.isSearchWithSubway(),
            tmpText: null,
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
            lastSearchMaxPriceChangeMS: 0,
            tmpMetroSelected: null,
            metros: MetrosStore.getMetros(),
            metrosSelected: []
        };
    },

    componentDidMount: function () {
        NavActions.navigateToHome();
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

            var formatted_address = place['name'];

            console.log('new location:');
            console.log(location);

            that.setState(assign(that.state, {
                location: location,
                countryCode: countryCode,
                bounds: bounds,
                formattedAddress: formatted_address
            }));
            MetrosActions.findMetros(location != null ? location.longitude : null, location != null ? location.latitude : null, countryCode, bounds);
            that.onClick();
        });

        SocialNetStore.addChangeListener(this.onSearchResultsChanged);
        MetrosStore.addChangeListener(this.onMetrosChanged);
        this.onClick();//trigger initial load
        MetrosActions.findMetros(null, null, null, null);
    },

    componentWillUnmount: function () {
        SocialNetStore.removeChangeListener(this.onSearchResultsChanged);
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

    onSearchResultsChanged: function () {
        console.log('on metros changed');
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
            var metrosSelected = this.state.metrosSelected;

            console.log('Searching for text: ' + text);
            SocialNetActions.findPosts(text, type, withSubway, oneRoomAptSelected, twoRoomAptSelected, threeRoomAptSelected, minPrice, maxPrice, location != null ? location.longitude : null, location != null ? location.latitude : null, countryCode, bounds, metrosSelected);
        }
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

    fireMetrosSelectedChange: function () {
        var metrosSelected = this.state.metrosSelected;

        SocialNetActions.changeSearchMetros(metrosSelected);
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
        SocialNetActions.changeSearchLocationInfo(null, null, null, null);
        SocialNetActions.changeSearchMetros([]);

        this.setState(assign(this.state, {
            withSubway: SocialNetStore.isSearchWithSubway(),
            type: SocialNetStore.getSearchType(),
            tmpText: null,
            text: SocialNetStore.getSearchText(),
            oneRoomAptSelected: SocialNetStore.getSearchRooms()["1"],
            twoRoomAptSelected: SocialNetStore.getSearchRooms()["2"],
            threeRoomAptSelected: SocialNetStore.getSearchRooms()["3"],
            location: SocialNetStore.getLocation(),
            countryCode: SocialNetStore.getCountryCode(),
            bounds: SocialNetStore.getBounds(),
            formattedAddress: SocialNetStore.getFormattedAddress(),
            //metro that's temporarily selected
            tmpMetroSelected: null,
            metrosSelected: SocialNetStore.getMetros()
        }));
    },

    clickOnEnter: function (e) {
        if (e.key == 'Enter') {
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
        var metrosSelected = this.state.metrosSelected;

        console.log('Searching for text: ' + text + "& min price: " + minPrice + " & max price " + maxPrice);

        SocialNetActions.resetFBSearchState();
        SocialNetActions.changeFBSearchText(text);
        SocialNetActions.changeFBSearchWithSubway(withSubway);
        SocialNetActions.changeFBSearchType(type);
        this.fireAptSelectionStateChange();
        SocialNetActions.changeFBSearchPrice(minPrice, maxPrice);
        SocialNetActions.changeSearchLocationInfo(location, countryCode, bounds, formattedAddress);
        SocialNetActions.changeSearchMetros(metrosSelected);
        SocialNetActions.findPosts(text, type, withSubway, oneRoomAptSelected, twoRoomAptSelected, threeRoomAptSelected, minPrice, maxPrice, location != null ? location.longitude : null, location != null ? location.latitude : null, countryCode, bounds, metrosSelected);
    },

    changeToLessor: function () {
        console.log('changeToLessor');
        this.onRentTypeChange('LESSOR');
    },

    changeToRenter: function () {
        console.log('changeToRenter');
        this.onRentTypeChange('RENTER');
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
                    <div className="col-md-11 col-sm-11 col-lg-11 pull-left">
                        <div className="col-sm-12 col-md-12 col-lg-12">
                                {_bubbles}
                        </div>
                    </div>
                </div>
            );
        }

        var tmpText = this.state.tmpText;

        return (
            <div>
                <div className="panel">
                    <div className="panel-body">

                        <form className="form-horizontal" role="form">
                            <div className='row'>
                                <div className="col-md-9 col-sm-9 col-lg-9">
                                    <RentType
                                        size='col-md-3 col-sm-3 col-lg-3'
                                        changeToRenter={this.changeToRenter}
                                        changeToLessor={this.changeToLessor}
                                    />

                                    <RoomsCount uiSize={4} uiLabelSize={5}

                                        oneRoomAptSelected={oneRoomAptSelected}
                                        twoRoomAptSelected={twoRoomAptSelected}
                                        threeRoomAptSelected={threeRoomAptSelected}

                                        onOneRoomAptValueChanged={this.onOneRoomAptValueChanged}
                                        onTwoRoomAptValueChanged={this.onTwoRoomAptValueChanged}
                                        onThreeRoomAptValueChanged={this.onThreeRoomAptValueChanged}
                                    />

                                    <PriceRange uiSize={5} uiLabelSize={3}

                                        minPrice={minPrice}
                                        maxPrice={maxPrice}

                                        onKeyPress={this.clickOnEnter}

                                        onMinPriceChange={this.onMinPriceChange}
                                        onMaxPriceChange={this.onMaxPriceChange}
                                    />
                                </div>
                            </div>

                            <div className='row'>
                                <div className="col-md-7 col-sm-7 col-lg-7">
                                    <div className="col-sm-12 col-md-12 col-lg-12">
                                        <AddressBox displayValue={formattedAddress} onAddressChange={this.onAddressChange}/>
                                    </div>
                                </div>

                                <div className="col-md-2 col-sm-2 col-lg-2">
                                    <MetroPopover
                                        metroInput={metrosDisplayItem}
                                        addButtonEnabled={this.state.tmpMetroSelected != null}
                                        onAddButtonClicked={this.onTargetMetroSelected}
                                    />
                                </div>
                                <div className="col-md-2 col-sm-2 col-lg-2">
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

                            <br/>

                            <div className='row'>
                                <div className='col-md-10 col-sm-10 col-lg-10'>

                                </div>

                                <div className="col-md-2 col-sm-2 col-lg-2">
                                    <a className="btn btn-primary center-block" onClick={this.onClick}>Найти</a>
                                </div>

                            </div>

                            <br/>

                            {bubbles}

                        </form>
                    </div>
                </div>

                <Posts items={items} shown={items.length > 0} hasMore={hasMoreResults} onHasMoreClicked={this.loadMoreResults} />
            </div>
        );
    }
});