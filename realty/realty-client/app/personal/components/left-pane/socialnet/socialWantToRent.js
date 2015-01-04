/**
 * Created by dionis on 04/01/15.
 */
var React = require('react');
var Util = require('rent4meUtil');
var assign = require('object-assign');

var SocialNetStore = require('../../../../shared/stores/SocialNetStore');
var SocialNetActions = require('../../../../shared/actions/SocialNetActions');

var _ = require('underscore');
var moment = require('moment');

var Posts = React.createClass({

    render: function () {
        var shown = this.props.shown || false;
        var items = this.props.items || [];
        var hasMore = this.props.hasMore || false;

        console.log("Has more?"+hasMore);

        var onHasMoreClicked = this.props.onHasMoreClicked;

        var hasMoreElement = hasMore ?
            (
                <a href="javascript:none;" onClick={onHasMoreClicked} className="list-group-item">

                    <p className="list-group-item-text">
                        Загрузить еще
                    </p>

                </a>
            ) : null;

        var style = {};
        if(!shown) {
            style['display'] = 'none';
        }

        var posts = items.map(function (item) {
            return (
                <Post item={item}/>
            );
        });

        return (
            <div className="col-md-9" style={style}>
                <div className="panel panel-default">
                    <div className="panel-heading">
                        <h4>Объявления</h4>
                    </div>

                    <div className="panel-body">

                        <div className="bs-component">
                            <div className="list-group">

                            {posts}

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

var Post = React.createClass({
    render: function () {
        var item = this.props.item || {};

        var firstImage = _.first(item.imageUrls.map(function(photo) {
            return (
                <img className="img-responsive" width="128"
                    src={photo}/>
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

                {imagePreviews}

                    <p className="list-group-item-text">

                        <h4>Описание</h4>

                        <div className="row">
                            <div className="col-md-12">
                            {item.message}
                            </div>

                        </div>

                        <hr/>

                        <div className="row">
                            <div className="col-md-6">
                                Добавлено: {moment(item.created).format("lll")}
                            </div>
                            <div className="col-md-6">
                                Обновлено: {moment(item.updated).format("lll")}
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
            SocialNetActions.findPosts(text);
        }
    },

    onSearchChange: function(e) {
        var value = e.target.value;

        this.setState(assign(this.state, {
            text: value
        }));
    },

    onClick: function(){
        var text = this.state.text;
        console.log('Searching for text: '+text);

        SocialNetActions.resetSearchState();
        SocialNetActions.findPosts(text);
    },

    render: function() {
        var items = this.state.posts || [];
        var hasMoreResults = this.state.hasMoreSearchResults || false;

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Объявления о сдаче</h4>


                        <form className="form-horizontal" role="form">
                            <div className="form-group">
                                <label className="col-md-2 control-label">Поиск</label>
                                <div className="col-md-10">
                                    <input type="text" className="form-control"
                                    placeholder="Введите текст для поиска по объявлениям"
                                    onChange={this.onSearchChange} ></input>
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