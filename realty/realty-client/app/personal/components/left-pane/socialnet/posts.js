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

var AdsItem = React.createClass({
    render: function () {
        return (
            <div className="row" style={{lineHeight: '32px'}}>
                <div className='col-sm-3 col-md-3 col-xs-3'>
                    <strong>{this.props.name}:</strong>
                </div>
                <div className='col-sm-9 col-md-9 col-xs-9'>
                    {this.props.text}
                </div>
            </div>
        )
    }
});

var Address = React.createClass({
    render: function () {
        var address = this.props.address;
        var addressIsSet = address && address.formatted_address;
        return (
            addressIsSet ? <AdsItem name='Адрес' text={address.formatted_address} /> : null
        )
    }
});

var MetroPreviews = React.createClass({

    getRecognized: function () {
        return this.props.metros.map(function (metro) {
            return metro.station_name + ";";
        });
    },

    render: function () {
        var hasMetros = _.size(this.props.metros) > 0;

        return (
            <AdsItem name='Метро' text={hasMetros ? this.getRecognized() : 'Не распознано'} />
        )
    }
});

var RoomCountInfo = React.createClass({
    render: function () {
        return (
            <AdsItem name='Комнат' text={this.props.roomCount ? this.props.roomCount : 'Не распознано'} />
        )
    }
});

var PriceInfo = React.createClass({
    render: function () {
        var item = this.props.item;
        return (
            <AdsItem name='Цена' text={item.rental_fee ? accounting.formatNumber(item.rental_fee, 0, " ") : 'не распознано'} />
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

    getExternalAuthorLink: function () {
        var item = this.props.item;
        return item.external_author_link ? (
            <div>
                <a href={item.external_author_link} target="_blank">Связаться через соц. сеть</a>
            </div>
        ) : null;
    },

    getSourceLink: function () {
        var item = this.props.item;
        return item.external_link ? (
            <div>
                <a href={item.external_link} target="_blank">Источник</a>
            </div>
        ) : null;
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
            <div className='col-sm-12 col-md-12 col-xs-12' style={{borderRadius: 2}}>
                <div>
                      {phoneNumbersDisplay}
                      {this.getDirectContact()}
                    <br/>
                      {this.getExternalAuthorLink()}
                    <br/>
                    {this.getSourceLink()}
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
            firstImage = item.external_images ? _.first(item.external_images.map(image => {
                return (
                    <img className="media-object" width="160"  src={image.small_thumbnail_url}/>
                );
            })) : null;
        }

        return firstImage;
    },

    render: function () {
        var withoutImage = (<img className="media-object"  src="http://placehold.it/160"/>);
        var firstImage = this.getFirstImage();
        return (
            <div>
                <div className="thumbnail pull-left">
                {firstImage ? firstImage : withoutImage}
                </div>
            </div>
        )
    }
});

var HeaderBlock = React.createClass({
    render: function () {
        var item = this.props.item;

        var creationDate = (
            <div>
                Дата объявления: {moment(item.created).format("lll")}
            </div>
        );

        return (
            <div className="panel-heading" style={{backgroundColor: 'rgba(207, 207, 207, 0.27)', borderBottom: 'none'}}>
                <div className='row'>
                    <div className='col-md-8 col-sm-8 col-xs-8'>
                        <p>{creationDate}</p>
                    </div>
                    <div className='col-md-4 col-sm-4 col-xs-4 text-right'>


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
            <div className="col-md-12 col-sm-12 col-xs-12">
                <div className="col-md-12 col-sm-12 col-xs-12"
                    style={{
                        boxShadow: 'none', lineHeight: '18px', fontSize: '14px', border: '1px #e4e4e4 solid',
                        padding: '15px', backgroundColor: 'rgba(244, 242, 242, 0.2)'
                    }}
                    dangerouslySetInnerHTML={{__html: Utils.nl2br(item.description)}}>
                </div>
            </div>
        )
    }
});

var IsAgent = React.createClass({

    getInitialState: function () {
        return {
            isAgent: false
        }
    },

    render: function () {
        //return(
        //
        //)
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
                    <div className="row">
                        <div className="col-md-3 col-sm-3 col-xs-3">
                            <ImagePreviews item={item} />
                        </div>
                        <div className="col-md-9 col-sm-9 col-xs-9">
                            <div className='col-lg-6 col-sm-6 col-md-6 col-xs-6'>
                                <Address address={item.address}/>
                                <MetroPreviews metros={item.metros}/>
                                <RoomCountInfo roomCount={item.room_count}/>
                                <PriceInfo item={item} />
                            </div>
                            <div className='col-md-6 col-md-6 col-xs-6'>
                                <div className="row bordered-element">
                                    <ContactInfo item={item} me={this.state.me} />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="row">
                        <Message item={item}/>
                    </div>
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