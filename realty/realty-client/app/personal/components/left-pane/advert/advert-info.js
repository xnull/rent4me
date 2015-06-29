/**
 * Advert info page
 * Created by null on 6/29/15.
 */
var React = require('react');

var AdvertInfo = React.createClass({
    componentDidMount: function () {
        this.initializeMap()
    },

    initializeMap: function () {
        var mapOptions = {
            center: new google.maps.LatLng(44.5403, -78.5463),
            zoom: 8,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        var map = new google.maps.Map(this.refs.googleMapComponent.getDOMNode(), mapOptions)
    },

    render: function () {
        return (
            <div>
                <!--  Панель управления с инфой о владельце объявления и кнопками для жалоб и связи -->
                <div className="row no-gutter">
                    <div className="col-md-12">
                        <div className="panel panel-default noBorderRadius"
                             style={{marginBottom: '0 !important', borderBottom: 0}}>
                            <div className="panel-body">
                                <div className="row">
                                    <!-- Фоточка разместившего объяву товарища (зареганного или фотка профиля из соц сети) -->
                                    <div className="col-xs-2 col-md-1">
                                        <a href="#">
                                            <!-- super man: http://mediad.publicbroadcasting.net/p/kpcw/files/201404/superman_no_photo.png -->
                                            <img src="https://nslnr.su/pict/no_photo_man.gif" alt="..."
                                                 className="img-rounded" width="38"/>
                                        </a>
                                    </div>

                                    <!-- Конпки связи -->
                                    <div className="col-xs-3 col-md-3 ">
                                        <a className="btn btn-danger center-block" style={{padding: '12px'}}>Связаться</a>
                                    </div>

                                    <!-- Редактировать -->
                                    <div className="col-xs-3 col-md-2 col-md-offset-3 text-center">
                                        <a className="btn btn-default">Исправить &nbsp;<img
                                            src="https://cdn0.iconfinder.com/data/icons/ikooni-outline-free-basic/128/free-18-24.png"
                                            className="glyphicon glyphicon-picture"
                                            title="Помочь в распознавании объявленияя"
                                            width="32"/>
                                        </a>
                                    </div>

                                    <!-- Пожаловаться -->
                                    <div className="col-xs-3 col-md-2 text-center">
                                        <a className="btn btn-default">Пожаловаться&nbsp;<img
                                            src="https://cdn0.iconfinder.com/data/icons/ikooni-outline-free-basic/128/free-18-24.png"
                                            className="glyphicon glyphicon-picture"
                                            title="Пожаловаться на объявление от агента"
                                            width="32"/>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="row">
                    <!-- Right panel -->
                    <div className="col-md-12 col-sm-12 col-xs-12">

                        <!-- adverts -->
                        <div className="row no-gutter">
                            <!-- Accomodation images -->
                            <div className="col-xs-12 col-md-8">
                                <div className="carousel slide" data-ride="carousel">
                                    <ol className="carousel-indicators">
                                        <li data-target="#carousel-example-generic" data-slide-to="0"
                                            className="active">
                                        </li>
                                        <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                                        <li data-target="#carousel-example-generic" data-slide-to="2"></li>
                                    </ol>

                                    <div className="item">
                                        <img style={{maxWidth: '100%'}}
                                             src="http://www.capsol.co.za/blog/wp-content/uploads/2012/05/luxury-camps-bay-holiday-accomodation.jpg"
                                             alt="..."/>
                                    </div>
                                </div>
                            </div>

                            <div className="col-md-4 col-xs-12">
                                <!-- the map -->
                                <div className="col-md-12">
                                    <div className="panel panel-default" style={{height: '305px'}}>
                                        <div className="panel-body">
                                            <div id="map-canvas" ref="googleMapComponent"
                                                 style={{position: 'absolute', left: '0px', top: '0px', overflow: 'hidden', width: '100%', height: '100%', zIndex: '0'}}>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Accomodation info -->
                                <table className="table table-striped">
                                    <tbody>
                                    <tr>
                                        <td className="col-md-12">Красноярская улица, Моск а, город Моск а</td>
                                    </tr>
                                    <tr>
                                        <td className="col-md-12">Комнат: 2</td>
                                    </tr>
                                    <tr>
                                        <td className="col-md-12">Метро Петро ско-разумо ская</td>
                                    </tr>
                                    <tr>
                                        <td className="col-md-12">Цена: 30 000 рублей</td>
                                    </tr>
                                    <tr>
                                        <td className="col-md-12">телефон: 8-900-123-123</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- Contacts -->
                        <div className="row no-gutter">
                            <div className="col-xs-12 col-md-12">
                                <table className="table table-striped">
                                    <tbody>
                                    <tr>
                                        <td>ОБЪЯВЛЕНИЕ С КОМИССИЕЙ за заселение. Комиссия посредника - 30% пер ого
                                            арендного платежа единоразо о

                                            Сдается: 3х ком к артира
                                            Адрес: ул. Крауля дом 6
                                            Цена : 23 000 + к.у. (льготные)

                                            Дополнительно: К артира на 2 этаже 9 этажном доме, хорошее состояние,
                                            есть се
                                            необходимое, мебель, быто ая техника. До центра 15 минут.

                                            Контактное лицо: Е гения

                                            Телефон: 8-908-639-55-99
                                            #agent@apartments_ekb

                                            Мы публикуем только реальные и про еренные эксклюзи ные арианты с
                                            комиссиями
                                            посреднико . Не более 15-20 шт. день. Они тоже кому-то нужны. Вот тут
                                            об этом
                                            подробно
                                            - http://vk.com/wall-51271270_47167.
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- Комментарии и лайки -->
                        <div className="row no-gutter">
                            <div className="col-md-4 col-md-offset-8 text-right">
                                <div className="row">
                                    <div className="col-xs-8">
                                        <h4>Поделиться </h4>
                                    </div>
                                    <div className="col-xs-4">
                                        <img
                                            src="https://cdn0.iconfinder.com/data/icons/social-flat-rounded-rects/512/vkontakte-32.png"
                                            width="32"/>
                                        <img
                                            src="https://cdn0.iconfinder.com/data/icons/social-flat-rounded-rects/512/facebook-32.png"
                                            width="32"/>
                                        <img
                                            src="https://cdn0.iconfinder.com/data/icons/social-flat-rounded-rects/512/odnoklassniki-32.png"
                                            width="32"/>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Виджет комментариев -->
                        <div className="row">
                            <div className="col-md-12">
                                <div className="panel panel-default noBorderRadius">
                                    <!-- добавление нового комментария -->
                                    <table className="table table-responsive table-striped table-borderless">
                                        <tbody>
                                        <tr>
                                            <td className="col-md-1 text-center">
                                                <a href="#">
                                                    <!-- super man: http://mediad.publicbroadcasting.net/p/kpcw/files/201404/superman_no_photo.png -->
                                                    <img src="https://nslnr.su/pict/no_photo_man.gif" alt="..."
                                                         className="img-circle" width="32"/>
                                                </a>
                                            </td>
                                            <td>
                                                <form>
                                                    <div className="form-group">
                                                        <label for="commentSystem">Ваш комментарий</label>
                                                        <input type="text" className="form-control"
                                                               id="commentSystem"
                                                               placeholder="текст комментария"/>
                                                    </div>
                                                </form>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>


                                    <!-- START list group. Списрк комментариев -->
                                    <div className="list-group">
                                        <!-- START list group item-->
                                        <a href="javascript:void(0);" className="list-group-item comment-group-item">

                                            <div className="media">
                                                <div className="pull-left">
                                                    <img
                                                        src="http://geedmo.com/themeforest/wintermin/app/img/user/01.jpg"
                                                        alt="Image"
                                                        className="media-object img-circle thumb48"/>
                                                </div>

                                                <div className="media-body clearfix">
                                                    <small className="pull-right">2h</small>
                                                    <strong className="media-heading text-primary">
                                                            <span
                                                                className="point point-success point-lg text-left"></span>Sheila
                                                        Carter</strong>

                                                    <p className="mb-sm">
                                                        <small>Cras sit amet nibh libero, in gravida nulla.
                                                            Nulla...
                                                        </small>
                                                    </p>
                                                </div>
                                            </div>
                                        </a>
                                        <!-- END list group item-->
                                        <!-- START list group item-->
                                        <a href="javascript:void(0);"
                                           className="list-group-item comment-group-item">

                                            <div className="media">
                                                <div className="pull-left">
                                                    <img
                                                        src="http://geedmo.com/themeforest/wintermin/app/img/user/04.jpg"
                                                        alt="Image"
                                                        className="media-object img-circle thumb48"/>
                                                </div>

                                                <div className="media-body clearfix">
                                                    <small className="pull-right">3h</small>
                                                    <strong className="media-heading text-primary">
                                                                <span
                                                                    className="point point-success point-lg text-left"></span>Rich
                                                        Reynolds</strong>

                                                    <p className="mb-sm">
                                                        <small>Cras sit amet nibh libero, in gravida
                                                            nulla.
                                                            Nulla...
                                                        </small>
                                                    </p>
                                                </div>
                                            </div>
                                        </a>
                                        <!-- END list group item-->
                                        <!-- START list group item-->
                                        <a href="javascript:void(0);"
                                           className="list-group-item comment-group-item">
                                            <div className="media">
                                                <div className="pull-left">
                                                    <img
                                                        src="http://geedmo.com/themeforest/wintermin/app/img/user/03.jpg"
                                                        alt="Image"
                                                        className="media-object img-circle thumb48"/>
                                                </div>

                                                <div className="media-body clearfix">
                                                    <small className="pull-right">4h</small>
                                                    <strong className="media-heading text-primary">
                                                                    <span
                                                                        className="point point-danger point-lg text-left"></span>Beverley
                                                        Pierce</strong>

                                                    <p className="mb-sm">
                                                        <small>Cras sit amet nibh libero, in gravida
                                                            nulla.
                                                            Nulla...
                                                        </small>
                                                    </p>
                                                </div>
                                            </div>
                                        </a>
                                        <!-- END list group item-->
                                        <!-- START list group item-->
                                        <a href="javascript:void(0);"
                                           className="list-group-item comment-group-item">
                                            <div className="media">
                                                <div className="pull-left">
                                                    <img
                                                        src="http://geedmo.com/themeforest/wintermin/app/img/user/06.jpg"
                                                        alt="Image"
                                                        className="media-object img-circle thumb48"/>
                                                </div>

                                                <div className="media-body clearfix">
                                                    <small className="pull-right">4h</small>
                                                    <strong
                                                        className="media-heading text-primary">
                                                                        <span
                                                                            className="point point-danger point-lg text-left"></span>Alex
                                                        Somar</strong>

                                                    <p className="mb-sm">
                                                        <small>Vestibulum pretium aliquam
                                                            scelerisque.
                                                        </small>
                                                    </p>
                                                </div>
                                            </div>
                                        </a>
                                        <!-- END list group item-->
                                    </div>
                                    <!-- END list group-->
                                    <!-- START panel footer-->
                                    <div className="panel-footer clearfix">
                                        <a href="javascript:void(0);" className="pull-left">
                                            <small>Показать се комментарии</small>
                                        </a>
                                    </div>
                                    <!-- END panel-footer-->
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = AdvertInfo;