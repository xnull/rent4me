/**
 * Created by dionis on 05/01/15.
 */
var React = require('react');

var _ = require('underscore');
var Utils = require('rent4meUtil');
var moment = require('moment');

var Post = React.createClass({
    render: function () {
        var item = this.props.item || {};

        var firstImage = _.first(item.img_urls.map(function (photo) {
            return (
                <img className="media-object" width="160"  src={photo}/>
                );
        }));


        var metroElements = item.metros.map(function (metro) {
            return (
                <li>{metro.station_name}</li>
                );
        });

        var hasMetros = _.size(item.metros) > 0;

        var metroPreviews = hasMetros ? (
            <div className="col-xs-6 col-lg-4">
                <h3 className="media-heading">Метро</h3>
                <div>
                    <ul className="list-unstyled">
                        {metroElements}
                    </ul>
                </div>
            </div>
            ) :
            (
                <div className="col-xs-6 col-lg-4">
                    <h3 className="media-heading">Метро</h3>
                    <div>
                    Не указано
                    </div>
                </div>
                );

        var priceInfo = item.rental_fee ? (
            <div>
            Цена: {item.rental_fee}
            </div>
            ) : null;

        var roomCountInfo = item.room_count ? (
            <div className="col-xs-6 col-lg-4">
                <h3 className="media-heading"> Комнат:</h3>
                <div>
                      {item.room_count}
                </div>
            </div>
            ) : (
            <div className="col-xs-6 col-lg-4">
                <h3 className="media-heading"> Комнат:</h3>
                <div>
                Не указано
                </div>
            </div>
            );

        var phoneNumber = item.phone_number ? (
            <div>
            Тел.: {item.phone_number.national_formatted_number || item.phone_number.raw_number }
            </div>
            ) : null;

        var contactInfo = (
            <div className="col-xs-6 col-lg-4">
                <h3 className="media-heading"> Контакты:</h3>
                <div>
                      {phoneNumber}
                Link FB
                Link VK
                </div>
            </div>
            );


        var imagePreviews = firstImage ? (
            <div>
                <div className="thumbnail pull-left">
                {firstImage}
                </div>
            </div>
            ) : (
            <div className="thumbnail pull-left">
                <img className="media-object"  src="http://placehold.it/160"/>
            </div>
            );

        var headerBlock = (
            <div className="row">
                <div className="col-lg-8">
                    <div className="panel-heading">
                        <p>{priceInfo}</p>
                    </div>
                </div>

                <div className="col-lg-4 text-right">
                    <a href="#" className="btn btn-xs btn-default">
                        <span className="glyphicon glyphicon-heart"></span>
                    Запомнить</a>
                </div>
            </div>
            );

        var message = (
            <div className="panel-body">
                <div className="row">
                    <div className="col-md-12" dangerouslySetInnerHTML={{__html: Utils.nl2br(item.message)}}>
                    </div>
                </div>
            </div>
            );

        var footer = (
            <div>
            Добавлено: {moment(item.created).format("lll")}
            </div>
            );


        return (
            <div className='panel panel-default'>
                    {headerBlock}
                <div className="panel-body">
                    <div className="well well-sm">
                        <div className="media">
                       {imagePreviews}
                            <div className="media-body">
                                <div className="row">
                                    {metroPreviews}
                                    {roomCountInfo}
                                    {contactInfo}
                                </div>
                            </div>
                        </div>
                        <hr/>
                    {message}
                    </div>
                {footer}
                </div>
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
                <a href="javascript:void(0)" onClick={onHasMoreClicked} className="list-group-item">

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
            <div style={style}>
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