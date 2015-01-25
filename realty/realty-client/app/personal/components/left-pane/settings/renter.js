/**
 * Created by null on 18.12.14.
 */
var React = require('react');
var NavActions = require('../../../../shared/actions/NavActions');

var RoomsCount = require('../../../../shared/ui/rooms-count');
var PriceRange = require('../../../../shared/ui/price-range');
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
                                <RoomsCount />

                                <div className='col-md-6'>
                                    <div className="control-group">
                                        <div className="dropdown">
                                            <button className="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
                                                Срок аренды
                                                <span className="caret"></span>
                                            </button>
                                            <ul className="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                                                <li role="presentation">
                                                    <a role="menuitem" tabindex="-1" href="#">Посуточно</a>
                                                </li>
                                                <li role="presentation">
                                                    <a role="menuitem" tabindex="-1" href="#">Долгосрочная</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <br/>

                            <div className='row'>
                                <PriceRange uiSize='5' uiLabelSize='2'/>
                            </div>

                            <hr/>

                            <div className='row'>
                                <div className='col-md-6'>
                                    <SearchAddress />
                                </div>
                            </div>

                            <br/>
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