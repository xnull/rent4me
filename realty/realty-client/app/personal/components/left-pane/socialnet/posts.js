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

var MetroPreviews = React.createClass({

    getMetroElements: function () {
        return this.props.metros.map(function (metro) {
            return (
                <li>{metro.station_name}</li>
            );
        });
    },

    getRecognized: function () {
        return (
            <div>
                <ul className="list-unstyled">
                        {this.getMetroElements()}
                </ul>
            </div>
        );
    },

    render: function () {
        var notRecognized = (<div>Не распознано</div>);
        var hasMetros = _.size(this.props.metros) > 0;

        return (
            <div className="col-xs-6 col-lg-4">
                <h3 className="media-heading">Метро</h3>
                {hasMetros ? this.getRecognized() : notRecognized}
            </div>
        )
    }
});

var RoomCountInfo = React.createClass({
    render: function () {
        var notRecognized = (<div>Не распознано</div>);
        var roomCount = (<div>{this.props.roomCount}</div>);

        return (
            <div className="col-xs-6 col-lg-4">
                <h3 className="media-heading"> Комнат:</h3>
                {this.props.roomCount ? roomCount : notRecognized}
            </div>
        )
    }
});

var ContactInfo = React.createClass({

    getDirectContact: function () {
        var item = this.props.item;
        var directContact;
        if (item.owner) {
            var me = this.props.me;

            console.log("Me:");
            console.log(me);

            var targetPerson = item.owner;

            var chatKey = Math.min(me.id, targetPerson.id) + '_' + Math.max(me.id, targetPerson.id);

            var url = "#/user/chat?id=" + chatKey + "&receiver_id=" + targetPerson.id;
            directContact = (<div>
                <a href={url} className="btn btn-default">Отправить сообщение</a>
            </div>);
        } else {
            directContact = null;
        }

        return directContact;
    },

    getExternalLink: function () {
        return (
            <div>
                <a href="http://vk.com/" target="_blank">Link (FB|VK)</a>
            </div>
        );
    },

    render: function () {
        var item = this.props.item;
        var hasContacts = item.contacts;

        var phoneNumbers = hasContacts ? item.contacts.filter(contact=>contact.type == 'PHONE').map(contact=>contact.phone) : [];

        var phoneNumbersDisplay = phoneNumbers.map(phone => {
            return (
                <div>
                    Тел.: {phone.national_formatted_number || phone.raw_number }
                </div>
            );
        });

        return (
            <div className="col-xs-6 col-lg-4">
                <h3 className="media-heading"> Контакты:</h3>
                <div>
                      {phoneNumbersDisplay}
                      {this.getDirectContact()}
                    <br/>
                      {this.getExternalLink()}
                </div>
            </div>
        )
    }
});

var ImagePreviews = React.createClass({
    getFirstImage: function () {
        var item = this.props.item;
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
    },

    render: function () {
        var withoutImage = (<img className="media-object"  src="http://placehold.it/160"/>);
        var firstImage = this.getFirstImage();
        return (
            <div>
                <div className="thumbnail pull-left" style={{marginBottom: 0}}>
                {firstImage ? firstImage : withoutImage}
                </div>
            </div>
        )
    }
});

var HeaderBlock = React.createClass({
    render: function () {
        var item = this.props.item;
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

        var footer = (
            <div>
                Добавлено: {moment(item.created).format("lll")}
            </div>
        );

        return (
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
        )
    }
});

var Message = React.createClass({
    render: function () {
        var item = this.props.item;
        return (
            <div className="panel-body" style={{padding: '15px 15px 0 15px'}}>
                <div className="row">
                    <div className="col-md-12 well"
                        style={{boxShadow: 'none', backgroundColor: 'rgba(250, 250, 250, 1)', marginBottom: 0}}
                        dangerouslySetInnerHTML={{__html: Utils.nl2br(item.description)}}>
                    </div>
                </div>
            </div>
        )
    }
});

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

        return (
            <div className='panel panel-info'>
                <HeaderBlock item={item}/>
                <div className="panel-body">
                    <div className="media">
                        <ImagePreviews item={item} />
                        <div className="media-body">
                            <div className="row">
                                <MetroPreviews metros={item.metros}/>
                                <RoomCountInfo roomCount={item.room_count}/>
                                <ContactInfo item={item} me={this.state.me} />
                            </div>
                        </div>
                    </div>
                    <Message item={item}/>
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