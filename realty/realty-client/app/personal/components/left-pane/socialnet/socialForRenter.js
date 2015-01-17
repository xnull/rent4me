/**
 * Created by dionis on 04/01/15.
 */
var React = require('react');
var Util = require('rent4meUtil');
var assign = require('object-assign');

var SocialNetStore = require('../../../../shared/stores/SocialNetRenterStore');
var SocialNetActions = require('../../../../shared/actions/SocialNetActions');

var RoomsCount = require('../../../../shared/ui/rooms-count');
var PriceRange = require('../../../../shared/ui/price-range');

var _ = require('underscore');
var moment = require('moment');
var Posts = require('./posts');

var RentType = React.createClass({
    render: function () {
        return (
            <div className='col-md-3 center-block'>
                <div className="btn-group" role="group" aria-label="...">
                    <button type="button" className="btn btn-default">Сниму</button>
                    <button type="button" className="btn btn-default">Сдам</button>
                </div>
            </div>
        )
    }
});

var RentTypee = React.createClass({
    render: function () {
        return (
            <div className="input-group">
                <div className='col-md-6'>
                    <input type="text" className="form-control center-block" placeholder="Username" aria-describedby="basic-addon1"/>
                </div>
                <div className='col-md-6 center-block'>
                    <input type="text" className="form-control center-block" placeholder="Username" aria-describedby="basic-addon1"/>
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
            posts: SocialNetStore.getSearchResults(),
            hasMoreSearchResults: SocialNetStore.hasMoreSearchResults()
        };
    },

    componentDidMount: function () {
        SocialNetStore.addChangeListener(this.onSearchResultsChanged);
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
            console.log('Searching for text: ' + text);
            SocialNetActions.findRenterPosts(text);
        }
    },

    onSearchChange: function (e) {
        var value = e.target.value;

        console.log('on search change');

        SocialNetActions.changeRenterSearchText(value);

        this.setState(assign(this.state, {
            text: value
        }));
    },

    onSubwayChange: function (e) {
        console.log(e);
        var value = e.target.checked;
        console.log("With subway new value: " + value);

        SocialNetActions.changeRenterSearchWithSubway(value);

        this.setState(assign(this.state, {
            withSubway: value
        }));
    },

    onClear: function () {
        SocialNetActions.resetRenterSearchState();
        SocialNetActions.changeRenterSearchText(null);
        SocialNetActions.changeRenterSearchWithSubway(false);

        this.setState(assign(this.state, {
            withSubway: SocialNetStore.isSearchWithSubway(),
            text: SocialNetStore.getSearchText()
        }));
    },

    onClick: function () {
        var text = this.state.text;
        var withSubway = this.state.withSubway;
        console.log('Searching for text: ' + text);

        SocialNetActions.resetRenterSearchState();
        SocialNetActions.changeRenterSearchText(text);
        SocialNetActions.changeRenterSearchWithSubway(withSubway);
        SocialNetActions.findRenterPosts(text, withSubway);
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
                        <h4>Объявления о сдаче</h4>

                        <form className="form-horizontal" role="form">
                            <div className='row'>
                                <div className="col-md-10 col-md-offset-1">
                                    <RentType />
                                    <RoomsCount />
                                    <PriceRange />
                                </div>
                            </div>
                            <br/>

                            <div className='row'>
                                <div className="col-md-10 col-md-offset-1">
                                    <div className="col-md-12">
                                        <input type="text" className="form-control" value={text}
                                            placeholder="Поиск по адресу, метро, улице, району"
                                            onChange={this.onSearchChange} >
                                        </input>
                                    </div>
                                </div>

                            </div>

                            <br/>

                            <div className='row'>
                                <div className="col-md-offset-6 col-md-3">
                                    <a className="btn btn-danger center-block" onClick={this.onClear}>Очистить</a>
                                </div>
                                <div className="col-md-3">
                                    <a className="btn btn-primary center-block" onClick={this.onClick}>Поиск</a>
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