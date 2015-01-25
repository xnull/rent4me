/**
 * Created by null on 18.12.14.
 */
var React = require('react');
var NavActions = require('../../../../shared/actions/NavActions');

var RoomsCount = require('../../../../shared/ui/rooms-count');
var RentPeriod = require('../../../../shared/ui/rent-period');
var PriceRange = require('../../../../shared/ui/price-range');
var Description = require('../../../../shared/ui/description-area');
var SearchAddress = require('../../../../shared/ui/search-address');

var Renter = React.createClass({
    componentDidMount: function () {
        NavActions.navigateToRenter();
    },
    render: function () {
        return (
            <div className="col-md-9">
                <div className="panel well">
                    <div className="panel-body">
                        <h4>Разместить объявление</h4>

                        <form className="form-horizontal" role="form">
                            <div className='row'>
                                <RentPeriod uiSize='10' uiLabelSize='2'/>
                            </div>

                            <br/>

                            <div className='row'>
                                <RoomsCount uiSize='10' uiLabelSize='2'/>
                            </div>
                            <br/>

                            <div className='row'>
                                <PriceRange uiSize='10' uiLabelSize='2' uiInputSizes='6'/>
                            </div>

                            <br/>

                            <div className='row'>
                                <SearchAddress uiSize='10' uiLabelSize='2'/>
                            </div>

                            <div className='row'>
                                <Description uiSize='10' uiLabelSize='2' uiInputSizes='6'/>
                            </div>

                            <br/>
                        </form>
                    </div>

                    <div className="panel-footer">
                        <div className='row'>
                            <div className="col-md-6 col-md-offset-3">
                                <a className="btn btn-success center-block">Продолжить</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = Renter;