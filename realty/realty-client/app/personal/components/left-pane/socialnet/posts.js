/**
 * Created by dionis on 05/01/15.
 */
var React = require('react');

var Utils = require('rent4meUtil');
var App = require('rent4meComponents');

var _ = Utils.libs.Underscore;
var moment = Utils.libs.Moment;
var accounting = Utils.libs.Accounting;
var assign = Utils.libs.ObjectAssign;
var UserStore = App.UserStore;
var UserActions = App.UserActions;
var ReactBootstrap = Utils.libs.ReactBootstrap;
var Carousel = ReactBootstrap.Carousel;
var CarouselItem = ReactBootstrap.CarouselItem;
var AdvertWidget = App.AdvertWidget;
var AdvertInfo = App.AdvertInfoPage;

var Router = Utils.libs.ReactRouter;
var Link = Router.Link;

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
        var address = Utils.formatPostItemAddress(this.props.item);
        //var address = this.props.address;
        var addressIsSet = !!address;
        return (
            addressIsSet ? <AdsItem name='Адрес' text={address}/> : null
        )
    }
});

var MetroPreviews = React.createClass({

    getRecognized: function () {
        var metros = this.props.metros.map(function (metro) {
            return (<li>{metro.station_name}</li>);
        });
        return (
            <ul>
                {metros}
            </ul>
        );
    },

    render: function () {
        var hasMetros = _.size(this.props.metros) > 0;

        return (
            <AdsItem name='Метро' text={hasMetros ? this.getRecognized() : "См. в описании"}/>
        )
    }
});

var GoogleMapView = React.createClass({

    map: null,
    listener: null,

    componentDidMount: function () {
        var location = this.props.location;

        if (location.latitude && location.longitude) {
            var latLng = new google.maps.LatLng(location.latitude, location.longitude);
            var mapOptions = {
                scrollwheel: false,
                zoom: 14,
                center: latLng
            };

            var map = new google.maps.Map(this.refs.mapInsert.getDOMNode(),
                mapOptions);

            this.listener = google.maps.event.addDomListener(window, 'resize', function () {
                map.setCenter(latLng);
            });

            this.map = map;


            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(location.latitude, location.longitude),
                map: map
            });
        }
    },

    componentWillUnmount: function () {
        if (this.listener) {
            google.maps.event.removeListener(this.listener);
        }
    },

    render: function () {
        return (
            <div ref="mapInsert" style={{width: '100%', height: '300px', marginBottom: '20px'}}></div>
        );
    }
});

var RoomCountInfo = React.createClass({
    render: function () {
        return (
            <AdsItem name='Комнат' text={this.props.roomCount ? this.props.roomCount : 'См. в описании'}/>
        )
    }
});

var PriceInfo = React.createClass({
    render: function () {
        var item = this.props.item;
        return (
            <AdsItem name='Цена'
                     text={item.rental_fee ? accounting.formatNumber(item.rental_fee, 0, " ") : 'См. в описании'}/>
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
                <a href={url} className="btn btn-default">Отпра ить сообщение</a>
            </div>);
        } else {
            directContact = null;
        }

        return directContact;
    },

    getExternalContactLinks: function () {
        var item = this.props.item;

        var externalLinkContent = item.external_link ? (
            <a href={item.external_link} target="_blank">Источник &nbsp;<i
                className="glyphicon glyphicon-new-window"></i> </a>
        ) : null;
        var externalAuthorContent = item.external_author_link ? (
            <span>
                    &nbsp;&nbsp;&nbsp;(<a href={item.external_author_link} target="_blank">
                <i title="Связаться через соц. сеть" className="glyphicon glyphicon-user"></i>
            </a>)
            </span>
        ) : null;

        return (item.external_author_link || item.external_link ) ? (
            <div>
                {externalLinkContent} {externalAuthorContent}
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
                    {this.getExternalContactLinks()}
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
                    <img className="media-object" width="160" src={photo.small_thumbnail_url}/>
                );
            })) : null;
        } else {
            firstImage = item.external_images ? _.first(item.external_images.map(image => {
                return (
                    <img className="media-object" width="160" src={image.small_thumbnail_url}/>
                );
            })) : null;
        }

        return firstImage;
    },

    getImageUrls: function () {
        var item = this.props.item;

        var images;

        if (item.data_source == 'INTERNAL') {
            images = item.photos ? item.photos.map(photo => {
                return (
                    photo.full_picture_url
                );
            }) : null;
        } else {
            images = item.external_images ? item.external_images.map(image => {
                return (
                    image.full_picture_url
                );
            }) : null;
        }

        return images;
    },

    render: function () {
        var showFull = this.props.showFull;
        var withoutImage = (<img className="media-object" src="/personal/images/not-available.jpeg" width="160"/>);
        var firstImage = this.getFirstImage();
        var images = this.getImageUrls();

        var carousel = images && images.length > 0 ? (
            <div className="row">
                <div className="col-xs-12">
                </div>
                <div className="col-xs-10 col-xs-offset-1">
                    <Carousel interval={false}>
                        {images.map((image) => {
                            return (
                                <CarouselItem>
                                    <img alt='900x500' src={image} className="center-block"
                                         style={{maxHeight: '400px', minHeight: '400px', clip: 'rect(0px, auto, 400px, 0px)'}}/>
                                </CarouselItem>
                            );
                        })}
                    </Carousel>
                </div>
            </div>
        ) : null;

        return showFull ? (carousel) : (
            <div>
                <div className='col-lg-3 col-md-3 col-sm-12 col-xs-12'>
                    <div className="thumbnail pull-left">
                        {firstImage ? firstImage : withoutImage}
                    </div>
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
                Дата объя ления: {moment(item.created).format("lll")}
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

var MessageDetails = React.createClass({
    render: function () {
        var item = this.props.item;
        var showFull = this.props.showFull;
        return (
            showFull ? null :
                <div className="col-md-offset-9 col-sm-offset-8 col-xs-offset-6 col-md-3 col-sm-4 col-xs-6"
                     style={{marginTop: 5}}>
                    <Link to="advert" params={{id: item.id}}><a href={"#/advert/"+item.id}
                                                                className="btn btn-default center-block">Подробнее</a></Link>
                </div>
        )
    }
});

var Message = React.createClass({
    render: function () {
        var item = this.props.item;
        var showFull = this.props.showFull;
        return (

            <div className="col-md-12 col-sm-12 col-xs-12">
                <div className="col-md-12 col-sm-12 col-xs-12"
                     style={{
                        boxShadow: 'none', lineHeight: '18px', fontSize: '14px', border: '1px #e4e4e4 solid',
                        padding: '15px', backgroundColor: 'rgba(244, 242, 242, 0.2)'
                    }}
                     dangerouslySetInnerHTML={{__html: Utils.nl2br(showFull ? item.description: Utils.previewText(item.description, 128))}}>
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

        var showFull = this.props.showFull || false;

        if (!showFull) {
            return <AdvertWidget item={this.props.item}/>
        } else {
            return <AdvertInfo item={this.props.item}/>
        }

        var gallery = <ImagePreviews item={item} showFull={showFull}/>;

        var mapInsert = showFull && item.location ? (<GoogleMapView location={item.location}/>) : null;

        //show in a better way for list & for full screen
        var classNamesForAddresses = showFull ? "col-md-12 col-sm-12 col-xs-12" : "col-md-9 col-sm-6 col-xs-12";

        return (
            <div className='panel panel-info'>
                <HeaderBlock item={item}/>

                <div className="panel-body">
                    <div className="row">
                        {gallery}
                        <div className={classNamesForAddresses}>
                            <div className='col-lg-6 col-md-6 col-sm-12 col-xs-12'>
                                <Address item={item}/>
                                <MetroPreviews metros={item.metros}/>
                                <RoomCountInfo roomCount={item.room_count}/>
                                <PriceInfo item={item}/>
                            </div>
                            <div className='col-md-6 col-sm-12 col-xs-12'>
                                <div className="row bordered-element">
                                    <ContactInfo item={item} me={this.state.me}/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="row">
                        <div className='col-md-12 col-sm-12 col-xs-12'>
                            <br/>
                            {mapInsert}
                        </div>
                    </div>

                    <div className="row">
                        <Message item={item} showFull={showFull}/>
                    </div>

                    <div className="row">
                        <MessageDetails item={item} showFull={showFull}/>
                    </div>
                </div>
            </div>
        );
    }
});

var Posts = React.createClass({

    getShownStyle: function () {
        var shown = this.props.shown || false;
        var style = {};
        if (!shown) {
            style['display'] = 'none';
        }
        return style
    },

    furtherAdverts: function () {
        var hasMore = this.props.hasMore || false;
        console.log("Has more?: " + hasMore);

        if (!hasMore) {
            return null;
        }

        var onHasMoreClicked = this.props.onHasMoreClicked;
        return (
            <a className="btn btn-default center-block" href="javascript:void(0)" onClick={onHasMoreClicked}>
                <h3>Далее</h3>
            </a>
        )
    },

    getPosts: function () {
        var items = this.props.items || [];
        var showFull = this.props.showFull;

        var posts = items.map(function (item) {
            return (
                <Post item={item} showFull={showFull}/>
            );
        });

        return posts;
    },

    render: function () {
        return (
            <div>
                <div className="row">
                    <div style={this.getShownStyle()}>
                        {this.getPosts()}
                    </div>
                </div>
                <div className="row">
                    <div className="col-xs-12 clo-sm-12 col-md-3 col-md-offset-9">
                        {this.furtherAdverts()}
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = Posts;
