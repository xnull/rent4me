/**
 * Created by dionis on 04/01/15.
 */
var React = require('react');
var Util = require('rent4meUtil');
var assign = require('object-assign');

var SocialNetStore = require('../../../../shared/stores/SocialNetFBStore');
var SocialNetActions = require('../../../../shared/actions/SocialNetActions');

var RoomsCount = require('../../../../shared/ui/rooms-count');
var PriceRange = require('../../../../shared/ui/price-range');

var _ = require('underscore');
var moment = require('moment');
var Posts = require('./posts');

var RentType = React.createClass({

    render: function () {
        var onRentTypeChange = this.props.onRentTypeChange;

        var changeToLessor = function () {
            onRentTypeChange('LESSOR');
        };

        var changeToRenter = function () {
            onRentTypeChange('RENTER');
        };

        var isRenter = SocialNetStore.getSearchType() === 'RENTER';

        var renterClasses = "btn btn-default" + (isRenter ? " active" : "");
        var lessorClasses = "btn btn-default" + (!isRenter ? " active" : "");


        return (
            <div className='col-md-3'>
                <div className="btn-group" data-toggle="buttons" >
                    <label className={renterClasses} onClick={changeToRenter} >
                        <input type="radio" >Снять</input>
                    </label>
                    <label className={lessorClasses} onClick={changeToLessor} >
                        <input type="radio" >Сдать</input>
                    </label>
                </div>
            </div>
        )
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
            minPrice: SocialNetStore.getSearchMinPrice(),
            maxPrice: SocialNetStore.getSearchMaxPrice(),
            hasMoreSearchResults: SocialNetStore.hasMoreSearchResults(),
            lastSearchTextChangeMS: 0,
            lastSearchMinPriceChangeMS: 0,
            lastSearchMaxPriceChangeMS: 0
        };
    },

    componentDidMount: function () {
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
            var withSubway = this.state.withSubway;
            var type = this.state.type;

            var oneRoomAptSelected = this.state.oneRoomAptSelected;
            var twoRoomAptSelected = this.state.twoRoomAptSelected;
            var threeRoomAptSelected = this.state.threeRoomAptSelected;

            var minPrice = this.state.minPrice;
            var maxPrice = this.state.maxPrice;

            console.log('Searching for text: ' + text);
            SocialNetActions.findFBPosts(text, type, withSubway, oneRoomAptSelected, twoRoomAptSelected, threeRoomAptSelected, minPrice, maxPrice);
        }
    },

    onSearchChange: function (e) {
        var value = e.target.value;

        console.log('on search change');

        SocialNetActions.changeFBSearchText(value);

        this.setState(assign(this.state, {
            text: value
        }));

        var now = (new Date()).getTime();
        var that = this;

        this.setState(assign(this.state, {
            lastSearchTextChangeMS: now
        }));

        setTimeout(function () {
            console.log('timed out');
            console.log('states value: ' + that.state.lastSearchTextChangeMS);
            console.log('remembered value: ' + now);
            if (that.state.lastSearchTextChangeMS == now) {
                that.onClick();
            }
        }, 500);
    },

    onSubwayChange: function (e) {
        console.log(e);
        var value = e.target.checked;
        console.log("With subway new value: " + value);

        SocialNetActions.changeFBSearchWithSubway(value);

        this.setState(assign(this.state, {
            withSubway: value
        }));

        this.onClick();
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

        var now = (new Date()).getTime();
        var that = this;

        this.setState(assign(this.state, {
            lastSearchMinPriceChangeMS: now
        }));

        setTimeout(function () {
            console.log('timed out');
            console.log('states value: ' + that.state.lastSearchMinPriceChangeMS);
            console.log('remembered value: ' + now);
            if (that.state.lastSearchMinPriceChangeMS == now) {
                that.onClick();
            }
        }, 500);
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

        var now = (new Date()).getTime();
        var that = this;

        this.setState(assign(this.state, {
            lastSearchMaxPriceChangeMS: now
        }));

        setTimeout(function () {
            console.log('timed out');
            console.log('states value: ' + that.state.lastSearchMinPriceChangeMS);
            console.log('remembered value: ' + now);
            if (that.state.lastSearchMaxPriceChangeMS == now) {
                that.onClick();
            }
        }, 500);
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
        SocialNetActions.changeFBSearchWithSubway(false);
        SocialNetActions.changeFBSearchRooms(false, false, false);
        SocialNetActions.changeFBSearchPrice(null, null);

        this.setState(assign(this.state, {
            withSubway: SocialNetStore.isSearchWithSubway(),
            type: SocialNetStore.getSearchType(),
            text: SocialNetStore.getSearchText(),
            oneRoomAptSelected: SocialNetStore.getSearchRooms()["1"],
            twoRoomAptSelected: SocialNetStore.getSearchRooms()["2"],
            threeRoomAptSelected: SocialNetStore.getSearchRooms()["3"]
        }));
    },

    onClick: function () {
        var text = this.state.text;
        var withSubway = this.state.withSubway;
        var type = this.state.type;
        var oneRoomAptSelected = this.state.oneRoomAptSelected;
        var twoRoomAptSelected = this.state.twoRoomAptSelected;
        var threeRoomAptSelected = this.state.threeRoomAptSelected;

        var minPrice = this.state.minPrice;
        var maxPrice = this.state.maxPrice;

        console.log('Searching for text: ' + text + "& min price: " + minPrice + " & max price " + maxPrice);

        SocialNetActions.resetFBSearchState();
        SocialNetActions.changeFBSearchText(text);
        SocialNetActions.changeFBSearchWithSubway(withSubway);
        SocialNetActions.changeFBSearchType(type);
        this.fireAptSelectionStateChange();
        SocialNetActions.changeFBSearchPrice(minPrice, maxPrice);
        SocialNetActions.findFBPosts(text, type, withSubway, oneRoomAptSelected, twoRoomAptSelected, threeRoomAptSelected, minPrice, maxPrice);
    },

    render: function () {
        var items = this.state.posts || [];
        var hasMoreResults = this.state.hasMoreSearchResults || false;
        var text = this.state.text || '';
        var withSubWay = this.state.withSubway || false;

        var oneRoomAptSelected = this.state.oneRoomAptSelected;
        var twoRoomAptSelected = this.state.twoRoomAptSelected;
        var threeRoomAptSelected = this.state.threeRoomAptSelected;

        var minPrice = this.state.minPrice;
        var maxPrice = this.state.maxPrice;

        console.log('with subway? ' + withSubWay);

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Поиск по социальным сетям</h4>

                        <form className="form-horizontal" role="form">
                            <div className='row'>
                                <div className="col-md-10 col-md-offset-1">
                                    <RentType onRentTypeChange={this.onRentTypeChange}/>
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

                                        onMinPriceChange={this.onMinPriceChange}
                                        onMaxPriceChange={this.onMaxPriceChange}
                                    />
                                </div>
                            </div>
                            <br/>

                            <div className="form-group">
                                <label className="col-md-2 control-label">
                                    С метро
                                </label>
                                <div className="col-md-10">
                                    <input
                                        type="checkbox"
                                        onChange={this.onSubwayChange}
                                        checked={withSubWay}
                                    />
                                </div>
                            </div>

                            <br/>

                            <div className='row'>
                                <div className="col-md-9 col-md-offset-1">
                                    <div className="col-md-12">
                                        <input type="text" className="form-control" value={text}
                                            placeholder="Поиск по адресу, метро, улице, району"
                                            onChange={this.onSearchChange} >
                                        </input>
                                    </div>
                                </div>
                                <div className="col-md-1">
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