var React = require('react');
var App = require('rent4meComponents');
var AdvertHelper = require('../socialnet/advert-widget-helper.js');
var ReactBootstrap = Utils.libs.ReactBootstrap;
var Carousel = ReactBootstrap.Carousel;
var CarouselItem = ReactBootstrap.CarouselItem;

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

            var map = new google.maps.Map(this.refs.googleMapComponent.getDOMNode(), mapOptions);

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
            <div id="map-canvas" ref="googleMapComponent"
                 style={{position: 'absolute', left: '0px', top: '0px', overflow: 'hidden', width: '100%', height: '100%', zIndex: '0'}}>
            </div>
        );
    }
});

var ImagePreviews = React.createClass({
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
        var images = this.getImageUrls();

        if (!images || images.length <= 0) {
            return null;
        }

        return (
            <div className="row">
                <div className="col-xs-12">
                    <Carousel interval={false}>
                        {images.map((image) => {
                            return (
                                <CarouselItem>
                                    <img className="img-responsive center-block"
                                         style={{maxWidth: '100%', minHeight: '510px', maxHeight: '510px',
                                         display: 'block', borderBottom: '1px solid'}}
                                         src={image} alt="..."
                                    />
                                </CarouselItem>
                            );
                        })}
                    </Carousel>
                </div>
            </div>
        )
    }
});

var AdvertContent = React.createClass({
    render: function () {
        var item = this.props.item;

        return (
            <div className="row no-gutter">
                <!-- Accomodation images -->
                <div className="col-xs-12 col-md-8">
                    <ImagePreviews item={this.props.item}/>
                </div>

                <div className="col-md-4 col-xs-12">
                    <!-- the map -->
                    <div className="col-md-12">
                        <div className="panel panel-default" style={{height: '305px'}}>
                            <div className="panel-body">
                                <GoogleMapView location={this.props.item.location}/>
                            </div>
                        </div>
                    </div>

                    <!-- Accomodation info -->
                    <table className="table table-striped">
                        <tbody>
                        <tr>
                            <td className="col-md-12">{AdvertHelper.getAddress(item)}</td>
                        </tr>
                        <tr>
                            <td className="col-md-12">Комнат {AdvertHelper.getRoomCount(item)}</td>
                        </tr>
                        <tr>
                            <td className="col-md-12">Метро {AdvertHelper.getMetroList(item.metros)}</td>
                        </tr>
                        <tr>
                            <td className="col-md-12">Цена {AdvertHelper.getPrice(item)} рублей</td>
                        </tr>
                        <tr>
                            <td className="col-md-12">телефон: {AdvertHelper.getPhone(item)}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
});

module.exports = AdvertContent;