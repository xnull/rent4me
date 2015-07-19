/**
 * A widget of some advert
 * Created by null on 6/21/15.
 */
var App = require('rent4meComponents');
var React = require('react');
var UserStore = require('../../../../shared/stores/UserStore');
var UserActions = require('../../../../shared/actions/UserActions');
var helper = require('./advert-widget-helper');
var assign = require('object-assign');
var accounting = require('accounting');

var AdvertWidget = React.createClass({
    propTypes: {
        item: React.PropTypes.object
    },

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

    roomCount: function () {
        return this.props.item.room_count ? this.props.item.room_count : '?'
    },

    price: function () {
        return this.props.item.rental_fee ? accounting.formatNumber(this.props.item.rental_fee, 0, " ") : '?'
    },

    render: function () {
        var advertData = this.props.item;
        var metros = advertData.metros;

        return (
            <div className="list-group">
                <div className="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                    <div className="panel panel-default">
                        <a href={'#/advert/' + advertData.id}>
                            <img className="img-responsive center-block"
                                 style={{width: '100%', height: '250px', display: 'block', borderBottom: '1px solid'}}
                                 src={helper.getFirstImageUrl(advertData)} alt="..."/>
                        </a>

                        <div className="panel-body"
                             style={{paddingTop: '10px', borderBottom: '1px solid #EFEFEF', paddingBottom: '5px'}}>
                            <div className="row row-fluid">
                                <div className="col-xs-11">
                                    <h5
                                        style={{whiteSpace: 'nowrap', marginTop: '0px', marginBottom: '0px'}}>
                                        {helper.getAddress(advertData)}
                                    </h5>
                                </div>
                                <div className="col-xs-11">
                                    <h6 style={{whiteSpace: 'nowrap', marginBottom: '5px', overflow: 'hidden', textOverflow: 'ellipsis'}}>
                                        Метро {helper.getMetroList(metros)}
                                    </h6>
                                </div>
                            </div>
                        </div>

                        <div className="panel-body" style={{paddingTop: '5px', paddingBottom: '10px'}}>
                            <div className="row row-fluid text-center">
                                <div className="col-xs-5">
                                    <p style={{marginBottom: '5px'}}>Цена</p>
                                    <h4 style={{color: 'rgba(236, 75, 17, 0.8)', marginBottom: '0px', marginTop: '0px'}}>{this.price()}</h4>
                                </div>
                                <div className="col-xs-5 col-xs-offset-2">
                                    <p style={{marginBottom: '5px'}}>Комнат</p>
                                    <h4 style={{color: '#000', marginBottom: '0px', marginTop: '0px'}}>{this.roomCount()}</h4>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = AdvertWidget;
