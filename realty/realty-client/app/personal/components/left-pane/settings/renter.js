/**
 * Created by null on 18.12.14.
 */
var React = require('react');
var NavActions = require('../../../../shared/actions/NavActions');
var MetroSearch = require('../../../../shared/ui/metro-search');

var Renter = React.createClass({
    componentDidMount: function () {
        NavActions.navigateToRenter();
    },
    render: function () {
        return (
            <div className="col-md-9">
                <div className="panel">
                    <div className="panel-body">
                        <h4>В поисках жилья</h4>


                        <form className="form-horizontal" role="form">
                            <div className='row'>
                                <div className='col-md-6'>
                                    <div className="col-md-2">
                                        <label className="control-label">Комнат</label>
                                    </div>
                                    <div className="col-md-10">
                                        <div className="btn-group" data-toggle="buttons">
                                            <label className="btn btn-default">
                                                <input type="checkbox">1</input>
                                            </label>
                                            <label className="btn btn-default">
                                                <input type="checkbox">2</input>
                                            </label>
                                            <label className="btn btn-default">
                                                <input type="checkbox">3</input>
                                            </label>
                                            <label className="btn btn-default">
                                                <input type="checkbox">4+</input>
                                            </label>
                                        </div>
                                    </div>
                                </div>

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
                                <div className='col-md-6'>
                                    <div className="col-md-2">
                                        <label className="control-label">Цена</label>
                                    </div>
                                    <div className="col-md-10">
                                        <div className="col-md-6">
                                            <input type="text" className="form-control col-md-3" placeholder="От"/>
                                        </div>

                                        <div className="col-md-6">
                                            <input type="text" className="form-control col-md-3" placeholder="До"/>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <hr/>

                            <div className='row'>
                                <div className='col-md-6'>
                                    <input type="text" className="form-control col-md-3" placeholder="Улица, район, адрес"/>
                                </div>
                            </div>

                            <br/>

                            <div className='row'>
                                <div className='col-md-6'>
                                    <MetroSearch />
                                </div>
                            </div>

                            <br/>
                            <br/>

                            <div className='row'>
                                <div className="col-md-6 col-md-offset-3">
                                    <a className="btn btn-primary center-block">Разместить объявление</a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = Renter;