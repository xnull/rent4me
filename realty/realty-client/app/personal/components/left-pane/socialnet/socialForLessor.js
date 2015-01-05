/**
 * Created by dionis on 04/01/15.
 */
var React = require('react');
var Util = require('rent4meUtil');
var assign = require('object-assign');

var SocialNetStore = require('../../../../shared/stores/SocialNetLessorStore');
var SocialNetActions = require('../../../../shared/actions/SocialNetActions');

var _ = require('underscore');
var moment = require('moment');
var Posts = require('./posts');

module.exports = React.createClass({
    getInitialState: function () {
        return {
            withSubway: false,
            text: null,
            posts: SocialNetStore.getSearchResults(),
            hasMoreSearchResults: SocialNetStore.hasMoreSearchResults()
        };
    },

    componentWillMount: function () {
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

    loadMoreResults: function() {
        if(this.state.hasMoreSearchResults){
            var text = this.state.text;
            console.log('Searching for text: '+text);
            SocialNetActions.findLessorPosts(text);
        }
    },

    onSearchChange: function(e) {
        var value = e.target.value;

        this.setState(assign(this.state, {
            text: value
        }));
    },

    onSubwayChange: function(e) {
        console.log(e);
        var value = e.target.checked;
        console.log("With subway new value: "+value);

        this.setState(assign(this.state, {
            withSubway: value
        }));
    },

    onClick: function(){
        var text = this.state.text;
        var withSubway = this.state.withSubway;
        console.log('Searching for text: '+text);

        SocialNetActions.resetLessorSearchState();
        SocialNetActions.findLessorPosts(text, withSubway);
    },

    render: function() {
        var items = this.state.posts || [];
        var hasMoreResults = this.state.hasMoreSearchResults || false;
        var text = this.state.text || '';
        var withSubWay = this.state.withSubway || false;

        console.log('with subway? '+withSubWay);

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Объявления о съёме</h4>


                        <form className="form-horizontal" role="form">
                            <div className="form-group">
                                <label className="col-md-2 control-label">Поиск</label>
                                <div className="col-md-10">
                                    <input type="text" className="form-control"
                                        value={text}
                                        placeholder="Введите текст для поиска по объявлениям"
                                        onChange={this.onSearchChange} ></input>
                                </div>
                            </div>

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


                            <div className="col-md-offset-9">
                                <a className="btn btn-primary center-block" onClick={this.onClick}>Поиск</a>
                            </div>
                        </form>
                    </div>
                </div>

                <Posts items={items} shown={items.length > 0} hasMore={hasMoreResults} onHasMoreClicked={this.loadMoreResults} />
            </div>
        );
    }
});