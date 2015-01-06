/**
 * Created by dionis on 05/01/15.
 */
var React = require('react');

var _ = require('underscore');
var moment = require('moment');

var Post = React.createClass({
    render: function () {
        var item = this.props.item || {};

        var firstImage = _.first(item.imageUrls.map(function (photo) {
            return (
                <img className="img-responsive" width="128" src={photo}/>
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
            <div className='panel'>
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

var Posts = React.createClass({

    render: function () {
        var shown = this.props.shown || false;
        var items = this.props.items || [];
        var hasMore = this.props.hasMore || false;

        console.log("Has more?" + hasMore);

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
        if (!shown) {
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

module.exports = Posts;