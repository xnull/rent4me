/**
 * A widget of some advert
 * Created by null on 6/21/15.
 */
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
        return this.props.item.roomCount ? this.props.item.roomCount : 'См. в описании'
    },

    price: function () {
        return this.props.item.rental_fee ? accounting.formatNumber(this.props.item.rental_fee, 0, " ") : 'См. в описании'
    },

    render: function () {
        var advertData = this.props.item;
        var metros = advertData.metros;

        return (
            <div className="col-sm-4 col-md-4">
                <div className="panel panel-default">
                    <img style={{maxWidth: '100%'}} src={helper.getFirstImageUrl(advertData)} alt="..."/>

                    <div className="panel-body">
                        <a href={'#/advertData/' + advertData.id}>
                            <div className="caption">
                                <div className="row row-fluid">
                                    <div className="col-xs-11"><h4>{helper.getAddress(advertData)}</h4></div>
                                    <div className="col-xs-11">
                                        <h5>Метро {helper.getMetroList(metros)}</h5>
                                    </div>
                                </div>
                                <div className="row row-fluid">
                                    <div className="col-xs-12">
                                        <div style={{borderTop: '1px dotted', marginBottom: '10px'}}></div>
                                    </div>
                                    <div className="col-xs-4 text-center" style={{borderRight: '1px dotted'}}>
                                        <strong>Комнат: {this.roomCount()}</strong>
                                    </div>
                                    <div className="col-xs-4 text-center" style={{borderRight: '1px dotted'}}>
                                        <strong>Цена: {this.price()}</strong>
                                    </div>
                                    <div className="col-xs-4 text-center">
                                        <strong>Тел.: {helper.getPhone(advertData)}</strong>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = AdvertWidget;
