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
            /*backgroundImage: "url('images/flats/kitchen1.jpg')",*/
            backgroundImage: "url('http://www.veskip.ru/images/property/22592__40P0163-01.jpg')",
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
                            <div className='col-sm-6 col-md-6 col-lg-6 col-centered'>
                                <div className="panel">
                                    <div className="panel-body">
                                        <form className="form" role="form">
                                            <div className='row'>
                                                <div className="col-sm-10 col-md-10 col-lg-10">
                                                    <RentType changeToRenter={this.changeToRenter} changeToLessor={this.changeToLessor}/>
                                                    <RoomsCount uiSize={4} uiLabelSize={3}
                                                        oneRoomAptSelected={this.state.oneRoomAptSelected}
                                                        twoRoomAptSelected={this.state.twoRoomAptSelected}
                                                        threeRoomAptSelected={this.state.threeRoomAptSelected}

                                                        onOneRoomAptValueChanged={this.onOneRoomAptValueChanged}
                                                        onTwoRoomAptValueChanged={this.onTwoRoomAptValueChanged}
                                                        onThreeRoomAptValueChanged={this.onThreeRoomAptValueChanged}

                                                    />
                                                    <PriceRange uiSize={5} uiLabelSize={2}
                                                        onKeyPress={this.performSearchOnEnter}

                                                        minPrice={this.state.minPrice}
                                                        maxPrice={this.state.maxPrice}

                                                        onMinPriceChange={this.onMinPriceChange}
                                                        onMaxPriceChange={this.onMaxPriceChange}
                                                    />
                                                </div>
                                            </div>

                                            <div className='row'>
                                                <div className="col-sm-12 col-md-12 col-lg-12">
                                                    <div className="col-sm-10 col-md-10 col-lg-10">
                                                        <input type="text" className="form-control" value={this.state.text}
                                                            onChange={this.onChangeSearchText}
                                                            onKeyPress={this.performSearchOnEnter}
                                                            placeholder="Поиск по тексту объявления"
                                                            style={{
                                                                borderRadius: 'inherit',
                                                                paddingTop: 0,
                                                                paddingBottom: 0
                                                            }}>
                                                        </input>
                                                    </div>
                                                    <div className="col-sm-2 col-md-2 col-lg-2">
                                                        <a className="btn btn-success center-block" onClick={this.performSearch}>Найти</a>
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