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

/**
 * Старая форма для добавления объявления. Она станет часть новой формы впоследствии.
 */
var Renter = React.createClass({
    componentDidMount: function () {
        NavActions.navigateToRenter();
    },
    render: function () {
        return (

            <div className="panel well">
                <div className="panel-body">
                    <h4>Разместить новое объявление</h4>

                    <form className="form-horizontal" role="form">
                        <div className='row'>
                            <RentPeriod uiSize='12' uiLabelSize='2'/>
                        </div>

                        <br/>

                        <div className='row'>
                            <RoomsCount uiSize='12' uiLabelSize='2'/>
                        </div>
                        <br/>

                        <div className='row'>
                            <PriceRange uiSize='12' uiLabelSize='2' uiInputSizes='6'/>
                        </div>

                        <div className='row'>
                            <SearchAddress uiSize='12' uiLabelSize='2'/>
                        </div>

                        <br/>

                        <div className='row'>
                            <Description uiSize='12' uiLabelSize='2' uiInputSizes='6'/>
                        </div>

                        <br/>

                        <div className='row'>
                            <div className="col-md-3 col-md-offset-6">
                                <a className="btn btn-default center-block">Отмена (скрыть форму)</a>
                            </div>
                            <div className="col-md-3">
                                <a className="btn btn-success center-block">Разместить</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

        );
    }
});

var Adverts = React.createClass({
    render: function () {
        return (
            <div className="panel well">
                <div className="panel-body">
                    <h4>Активное бъявление</h4>
                    <div className='row'>
                    {/* example item */}
                        <div className='col-md-8'>
                            <p className='text'> однокомнатная квартира </p>
                            <p className='text'> долгосрочная аренда </p>
                            <p className='text'> адрес: такой-то </p>
                        </div>
                        <div className='col-md-4'>
                            <div className='col-md-6'>
                                <a className="btn btn-primary">Редактировать</a>
                            </div>
                            <div className='col-md-6'>
                                <a className="btn btn-danger">Удалить</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

/**
 * 1. Суть компонента: при заходе на страницу арендатор видит большую кнопку с плюсиком для добавления своего
 * объявления,
 * 2. при нажатии на кнопку ему отображается модальный диалог (см выше компонент Renter) с формой
 * для заполнения чего он хочет найти.
 * 3. При нажатии на кнопку сохранить в форме, форма исчезает и под большой кнопкой с плюсиком появляется
 * узкая панелька содержащая инфо о выставленном объявлении, а справа внутри панельки две кнопки: редактировать и удалить
 */
var NewRenter = React.createClass({

    getInitialState: function () {
        return {showAddAdvert: false};
    },

    onAddAdvert: function () {
        this.setState({showAddAdvert: true});
    },

    render: function () {
        return (
            <div className="col-md-9">
                <div className="panel">
                    <div className="panel-body">
                        <div className="col-md-2">
                            <div className="col-md-3">
                                <a
                                    className="thumbnail"
                                    style={{height: 240, width: 120, display: 'flex', alignItems: 'center'}}
                                    onClick={this.onAddAdvert}
                                >
                                    <img alt="..." src="images/blue-plus.png" width="32" height="32"/>
                                </a>
                            </div>
                        </div>

                        <div className="col-md-10">
                            { this.state.showAddAdvert ? <Renter /> : null }
                        </div>

                    </div>
                </div>

                <Adverts />
            </div>
        );
    }
});

module.exports = NewRenter;