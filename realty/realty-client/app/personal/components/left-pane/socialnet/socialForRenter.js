/**
 * Created by dionis on 04/01/15.
 */
var React = require('react');
var Util = require('rent4meUtil');
var assign = require('object-assign');

var SocialNetStore = require('../../../../shared/stores/SocialNetRenterStore');
var SocialNetActions = require('../../../../shared/actions/SocialNetActions');

var _ = require('underscore');
var moment = require('moment');
var Posts = require('./posts');

module.exports = React.createClass({
    getInitialState: function () {
        return {
            withSubway: SocialNetStore.isSearchWithSubway(),
            text: SocialNetStore.getSearchText(),
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
                                <div className='col-md-6'>
                                    <div className="col-md-2">
                                        <label className="control-label">Комнат</label>
                                    </div>
                                    <div className="col-md-10">
                                        <div className="btn-group" data-toggle="buttons">
                                            <label className="btn btn-default">
                                                <input type="checkbox">1</input>
                                            </label>
                                            <label className="btn btn-default">
                                                <input type="checkbox">2</input>
                                            </label>
                                            <label className="btn btn-default">
                                                <input type="checkbox">3</input>
                                            </label>
                                            <label className="btn btn-default">
                                                <input type="checkbox">4+</input>
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <div className='col-md-6'>
                                    <div className="control-group">
                                        <div className="dropdown">
                                            <button className="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
                                                Срок аренды
                                                <span className="caret"></span>
                                            </button>
                                            <ul className="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                                                <li role="presentation">
                                                    <a role="menuitem" tabindex="-1" href="#" style={{focus: {backgroundColor: '#000000'}}}>Посуточно</a>
                                                </li>
                                                <li role="presentation">
                                                    <a role="menuitem" tabindex="-1" href="#">Месяц</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br/>

                            <div className='row'>
                                <div className='col-md-6'>
                                    <div className="col-md-2">
                                        <label className="control-label">Цена</label>
                                    </div>
                                    <div className="col-md-10">
                                        <div className="col-md-6">
                                            <input type="text" className="form-control col-md-3" placeholder="От"/>
                                        </div>

                                        <div className="col-md-6">
                                            <input type="text" className="form-control col-md-3" placeholder="До"/>
                                        </div>
                                    </div>
                                </div>

                                <div className='col-md-6'>
                                    <div className="input-group">
                                        <input type="text" className="form-control" aria-label="..." placeholder="Станция метро"/>
                                        <div className="input-group-btn">
                                            <button type="button" className="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Выбрать
                                                <span className="caret"></span>
                                            </button>
                                            <ul className="dropdown-menu dropdown-menu-right" role="menu">
                                                <li>
                                                    <a href="#">Станция цветной бульвар</a>
                                                </li>
                                                <li>
                                                    <a href="#">Новослободская</a>
                                                </li>
                                                <li>
                                                    <a href="#">1905 года</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>


                            <hr/>

                            <div className='row'>
                                <div className='col-md-12'>
                                    <div className="form-group">
                                        <label className="col-md-2 control-label">Поиск</label>
                                        <div className="col-md-10">
                                            <input type="text" className="form-control"
                                                value={text}
                                                placeholder="Адрес, улица, район, метро"
                                                onChange={this.onSearchChange} ></input>
                                        </div>
                                    </div>
                                </div>

                                <div className='col-md-6'>
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
                                </div>


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