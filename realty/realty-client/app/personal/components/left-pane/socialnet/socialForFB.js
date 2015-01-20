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
                        <input type="radio" >Сниму</input>
                    </label>
                    <label className={lessorClasses} onClick={changeToLessor} >
                        <input type="radio" >Сдам</input>
                    </label>
                </div>
            </div>
        )
    }
});

var RentTypee = React.createClass({
    render: function () {
        return (
            <div class="btn-group input-group btn-group-justified">
                <input type="text" className="form-control" placeholder="От"/>
                <input type="text" className="form-control" placeholder="Отnnnn"/>
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
            hasMoreSearchResults: SocialNetStore.hasMoreSearchResults()
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
            console.log('Searching for text: ' + text);
            SocialNetActions.findFBPosts(text, type, withSubway);
        }
    },

    onSearchChange: function (e) {
        var value = e.target.value;

        console.log('on search change');

        SocialNetActions.changeFBSearchText(value);

        this.setState(assign(this.state, {
            text: value
        }));

        this.onClick();
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

    onClear: function () {
        SocialNetActions.resetFBSearchState();
        SocialNetActions.changeFBSearchText(null);
        SocialNetActions.changeFBSearchType(null);
        SocialNetActions.changeFBSearchWithSubway(false);

        this.setState(assign(this.state, {
            withSubway: SocialNetStore.isSearchWithSubway(),
            type: SocialNetStore.getSearchType(),
            text: SocialNetStore.getSearchText()
        }));
    },

    onClick: function () {
        var text = this.state.text;
        var withSubway = this.state.withSubway;
        var type = this.state.type;
        console.log('Searching for text: ' + text);

        SocialNetActions.resetFBSearchState();
        SocialNetActions.changeFBSearchText(text);
        SocialNetActions.changeFBSearchWithSubway(withSubway);
        SocialNetActions.changeFBSearchType(type);
        SocialNetActions.findFBPosts(text, type, withSubway);
    },

    render: function () {
        var items = this.state.posts || [];
        var hasMoreResults = this.state.hasMoreSearchResults || false;
        var text = this.state.text || '';
        var withSubWay = this.state.withSubway || false;

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
                                    <RoomsCount />
                                    <PriceRange />
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