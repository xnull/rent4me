/**
 * Created by dionis on 05/01/15.
 */
var React = require('react');

var _ = require('underscore');
var Utils = require('rent4meUtil');
var moment = require('moment');
var accounting = require('accounting');
var assign = require('object-assign');
var UserStore = require('../../../../shared/stores/UserStore');
var UserActions = require('../../../../shared/actions/UserActions');

var Post = React.createClass({

    getInitialState: function () {
        return {
            me: UserStore.getMyProfile()
        };
    },

    componentDidMount: function () {
        UserStore.addChangeListener(this.meLoadListener);
        UserActions.loadMyProfileIfNotLoaded();
    },

    componentWillUnmount: function () {
        UserStore.removeChangeListener(this.meLoadListener);
    },

    meLoadListener: function () {
        this.setState(assign(this.state, {
            me: UserStore.getMyProfile()
        }));
    },

    render: function () {
        var item = this.props.item || {};


        var firstImage;

        if (item.data_source == 'INTERNAL') {
            firstImage = item.photos ? _.first(item.photos.map(photo => {
                return (
                    <img className="media-object" width="160"  src={photo.small_thumbnail_url}/>
                );
            })) : null;
        } else {
            firstImage = item.img_urls ? _.first(item.img_urls.map(function (photo) {
                return (
                    <img className="media-object" width="160"  src={photo}/>
                );
            })) : null;
        }


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
                        Не распознано
                    </div>
                </div>
            );

        var priceInfo = (
            <div>
                Цена: {item.rental_fee ? accounting.formatNumber(item.rental_fee, 0, " ") : 'не распознано'}
            </div>
        );

        var addressInfo = (
            <div>
                {item.address && item.address.formatted_address ? item.address.formatted_address : ''}
            </div>
        );

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
                    Не распознано
                </div>
            </div>
        );

        var hasContacts = !!item.contacts;

        var phoneNumbers = hasContacts ? item.contacts.filter(contact=>contact.type == 'PHONE').map(contact=>contact.phone) : [];

        var phoneNumbersDisplay = phoneNumbers.map(phone => {
            return (
                <div>
                    Тел.: {phone.national_formatted_number || phone.raw_number }
                </div>
            );
        });

        var directContact;
        if (item.owner) {
            var me = this.state.me;

            console.log("Me:");
            console.log(me);

            var targetPerson = item.owner;

            var chatKey = Math.min(me.id, targetPerson.id)+'_'+Math.max(me.id, targetPerson.id);

            var url = "#/user/chat?id=" + chatKey + "&receiver_id=" + targetPerson.id;
            directContact = (<div>
                                <a href={url} className="btn btn-default">Отправить сообщение</a>
                            </div>);
        } else {
            directContact = null;
        }

        var externalLink = (<div><a href="http://vk.com/" target="_blank">Link (FB|VK)</a></div>);

        var contactInfo = (
            <div className="col-xs-6 col-lg-4">
                <h3 className="media-heading"> Контакты:</h3>
                <div>
                      {phoneNumbersDisplay}
                      {directContact}
                      <br/>
                      {externalLink}
                </div>
            </div>
        );


        var imagePreviews = firstImage ? (
            <div>
                <div className="thumbnail pull-left" style={{marginBottom: 0}}>
                {firstImage}
                </div>
            </div>
        ) : (
            <div className="thumbnail pull-left" style={{marginBottom: 0}}>
                <img className="media-object"  src="http://placehold.it/160"/>
            </div>
        );

        var footer = (
            <div>
                Добавлено: {moment(item.created).format("lll")}
            </div>
        );

        var headerBlock = (
            <div className="panel-heading" style={{backgroundColor: 'rgba(207, 207, 207, 0.27)', borderBottom: 'none'}}>
                <div className='row'>
                    <div className='col-md-2'>
                        <p>{priceInfo}</p>
                    </div>
                    <div className='col-md-6'>
                        <p>{addressInfo}</p>
                    </div>
                    <div className='col-md-4'>
                        <p>{footer}</p>
                    </div>
                </div>
            </div>
        );

        var message = (
            <div className="panel-body" style={{padding: '15px 15px 0 15px'}}>
                <div className="row">
                    <div className="col-md-12 well"
                        style={{boxShadow: 'none', backgroundColor: 'rgba(250, 250, 250, 1)', marginBottom: 0}}
                        dangerouslySetInnerHTML={{__html: Utils.nl2br(item.description)}}>
                    </div>
                </div>
            </div>
        );


        return (
            <div className='panel panel-info'>
                    {headerBlock}
                <div className="panel-body">
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
                    {message}
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
                <div className="list-group">
                    {posts}
                    <br/>
                    {hasMoreElement}
                </div>
            </div>
        );
    }
});

module.exports = Posts;