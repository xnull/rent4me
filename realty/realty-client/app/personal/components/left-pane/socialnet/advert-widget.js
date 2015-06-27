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
            <div className="col-sm-3 col-md-3">
                <div className="panel panel-default">
                    <a href={'#/advertData/' + advertData.id}>
                        <img className="img-responsive center-block"
                             style={{width: '100%', height: '200px', display: 'block', borderBottom: '1px dotted'}}
                             src={helper.getFirstImageUrl(advertData)} alt="..."/>
                    </a>

                    <div className="panel-body">
                        <div className="caption">
                            <div className="row row-fluid">
                                <div className="col-xs-11"><h5>{helper.getAddress(advertData)}</h5></div>
                                <div className="col-xs-11">
                                    <h6>Метро {helper.getMetroList(metros)}</h6>
                                </div>
                            </div>
                            <div className="row row-fluid">
                                <div className="col-xs-12">
                                    <div style={{borderTop: '1px dotted', marginBottom: '10px', whiteSpace: 'nowrap'}}></div>
                                </div>
                                /*<div className="col-xs-4 text-center" style={{borderRight: '1px dotted', whiteSpace: 'nowrap'}}>
                                    Комнат: {this.roomCount()}
                                </div>
                                <div className="col-xs-4 text-center" style={{borderRight: '1px dotted', whiteSpace: 'nowrap'}}>
                                    Цена: {this.price()}
                                </div>
                                <div className="col-xs-4 text-center">
                                    Тел.: {helper.getPhone(advertData)}
                                </div>*/
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = AdvertWidget;
