/**
 * Created by null on 03.01.15.
 */

var React = require('react');
var AuthComponent = require('../../shared/components/socialNetAuth');
var Utils = require('rent4meUtil');

var RoomsCount = require('../../shared/ui/rooms-count');
var PriceRange = require('../../shared/ui/price-range');
var RentType = require('../../shared/ui/rent-type');


var HeaderComponent = React.createClass({

    performSearch: function() {
        Utils.navigateToPersonal();
    },

    render: function () {

        var loginButtonStyle = {
            margin: 30,
            background: 'rgba(74, 35, 23, 0.4)'
        };


        var authComponentDisplayItem = (<input type="button" className="button special" value="Вход / Регистрация" style={loginButtonStyle}/>);

        var sectionStyle = {
            backgroundImage: "url('images/flats/kitchen1.jpg')",
            backgroundSize: 'cover',
            backgroundPosition: 'auto',
            backgroundRepeat: 'no-repeat'
        };
        return (
            <div style={sectionStyle}>
                <div>
                    <div style={{textAlign: 'right'}}>
                        <AuthComponent displayItem={authComponentDisplayItem}/>
                    </div>
                </div>
                <section id='header' className='dark'>
                    <header>
                        <h1 style={{color: '#da5037'}}>Аренда недвижимости</h1>
                        <p>Прозрачность, отсутствие посредников</p>
                    </header>
                    <footer>
                        <div className="row">
                            <div className='col-centered'>
                                <div className="panel">
                                    <div className="panel-body">
                                        <form className="form" role="form">
                                            <div className='row'>
                                                <div className="col-md-10">
                                                    <RentType/>
                                                    <RoomsCount uiSize='4' uiLabelSize='3' />
                                                    <PriceRange uiSize='5' uiLabelSize='2' />
                                                </div>
                                            </div>

                                            <div className='row'>
                                                <div className="col-md-12">
                                                    <div className="col-md-10">
                                                        <input type="text" className="form-control" value="you"
                                                            placeholder="Поиск по адресу, метро, улице, району"
                                                            style={{borderRadius: 'inherit'}}>
                                                        </input>
                                                    </div>
                                                    <div className="col-md-2">
                                                        <a className="btn btn-success center-block" onClick={this.performSearch()}>Найти</a>
                                                    </div>
                                                </div>
                                            </div>

                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </footer>
                </section>
            </div>
        )
    }
});

module.exports = HeaderComponent;