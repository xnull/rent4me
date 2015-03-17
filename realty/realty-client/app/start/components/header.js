/**
 * Created by null on 03.01.15.
 */

var React = require('react');
var AuthComponent = require('../../shared/components/socialNetAuth');
var SocialNetActions = require('../../shared/actions/SocialNetActions');
var Utils = require('rent4meUtil');
var Cookies = require('rent4meCookies');

var assign = require('object-assign');

var RoomsCount = require('../../shared/ui/rooms-count');
var PriceRange = require('../../shared/ui/price-range');
var RentType = require('../../shared/ui/rent-type');

var Cards = require('./cards');

var HeaderComponent = React.createClass({

    getInitialState: function () {
        return {
            oneRoomAptSelected: false,
            twoRoomAptSelected: false,
            threeRoomAptSelected: false,
            minPrice: null,
            maxPrice: null
        }
    },

    componentDidMount: function () {
        SocialNetActions.resetFBSearchState();
    },

    onOneRoomAptValueChanged: function (value) {
        this.setState(assign(this.state, {
            oneRoomAptSelected: value
        }));

        this.fireAptSelectionStateChange();
    },

    onTwoRoomAptValueChanged: function (value) {
        this.setState(assign(this.state, {
            twoRoomAptSelected: value
        }));

        this.fireAptSelectionStateChange();
    },

    onThreeRoomAptValueChanged: function (value) {
        this.setState(assign(this.state, {
            threeRoomAptSelected: value
        }));

        this.fireAptSelectionStateChange();
    },

    fireAptSelectionStateChange: function () {
        var oneRoomAptSelected = this.state.oneRoomAptSelected;
        var twoRoomAptSelected = this.state.twoRoomAptSelected;
        var threeRoomAptSelected = this.state.threeRoomAptSelected;

        SocialNetActions.changeFBSearchRooms(oneRoomAptSelected, twoRoomAptSelected, threeRoomAptSelected);
    },

    performSearch: function () {
        Utils.navigateToPersonal();
    },

    performSearchOnEnter: function (e) {
        if (e.key == 'Enter') {
            this.performSearch();
        }
    },

    changeToLessor: function () {
        console.log('changeToLessor');
        SocialNetActions.changeFBSearchType('LESSOR');
    },

    changeToRenter: function () {
        console.log('changeToRenter');
        SocialNetActions.changeFBSearchType('RENTER');
    },

    onChangeSearchText: function (event) {
        var text = event.target.value;

        this.setState(assign(this.state, {
            text: text
        }));
        SocialNetActions.changeFBSearchText(text);
    },

    onMinPriceChange: function (e) {
        var value = e.target.value;
        console.log("With min price new value: " + value);

        var minPrice = value;
        var maxPrice = this.state.maxPrice;

        SocialNetActions.changeFBSearchPrice(minPrice, maxPrice);

        this.setState(assign(this.state, {
            minPrice: minPrice
        }));
    },

    onMaxPriceChange: function (e) {
        var value = e.target.value;
        console.log("With max price new value: " + value);

        var minPrice = this.state.minPrice;
        var maxPrice = value;

        SocialNetActions.changeFBSearchPrice(minPrice, maxPrice);

        this.setState(assign(this.state, {
            maxPrice: maxPrice
        }));
    },

    render: function () {

        var loginButtonStyle = {
            margin: 30,
            /*background: 'rgba(74, 35, 23, 0.4)'*/
            background: 'rgba(3, 3, 3, 0.45)'
        };


        var authComponentDisplayItem = (<input type="button" className="button special" value="Вход / Регистрация" style={loginButtonStyle}/>);

        var sectionStyle = {
            backgroundImage: "url('images/signin/22592__40P0163-01.jpg')",
            /*backgroundImage: "url('http://www.veskip.ru/images/property/22592__40P0163-01.jpg')",*/
            backgroundSize: 'cover',
            backgroundPosition: 'auto',
            /*backgroundRepeat: 'no-repeat'*/
            backgroundRepeat: 'round'
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
                        <p>
                            <strong style={{color: 'rgb(168, 129, 66)'}}>Прозрачность, отсутствие посредников</strong>
                        </p>
                    </header>
                    <footer>
                        <div className="row">
                            <div className='col-sm-6 col-md-6 col-lg-8 col-centered'>
                                <div className="panel">
                                    <div className="panel-body">
                                        <form className="form" role="form">

                                            <div className="row">
                                                <div className='col-sm-12 col-md-3 col-lg-4 col-centered'>
                                                    <div className="btn-group btn-group-justified" role="group" data-toggle="buttons">
                                                        <label className="input-group-addon label-success">
                                                        Я хочу
                                                        </label>
                                                        <label type="radio" className="btn btn-default">Снять</label>
                                                        <label type="radio" className="btn btn-default">Сдать</label>
                                                    </div>
                                                </div>

                                                <div className='col-sm-12 col-md-3 col-lg-4 col-centered'>

                                                    <div className="btn-group btn-group-justified" data-toggle="buttons">
                                                        <label className="input-group-addon label-success">
                                                        Комнат
                                                        </label>
                                                        <label className="btn btn-default">
                                                            <input type="checkbox"
                                                            checked="btn btn-default">1</input>
                                                        </label>
                                                        <label className="btn btn-default">
                                                            <input type="checkbox" >2</input>
                                                        </label>
                                                        <label className="btn btn-default">
                                                            <input type="checkbox">3+</input>
                                                        </label>
                                                    </div>

                                                </div>

                                                <div className='col-sm-12 col-md-3 col-lg-4 col-centered'>
                                                    <div className="input-group ">
                                                        <span className="input-group-addon label-success" id="sizing-addon1">Цена</span>
                                                        <input type="text" className="form-control" placeholder="От:" aria-describedby="basic-addon1"/>
                                                        <span className="input-group-addon label-success" id="sizing-addon1">:</span>
                                                        <input type="text" className="form-control" placeholder="До:" aria-describedby="basic-addon1"/>
                                                    </div>
                                                </div>
                                            </div>

                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <Cards />
                    </footer>
                </section>
            </div>
            )
    }
});

module.exports = HeaderComponent;