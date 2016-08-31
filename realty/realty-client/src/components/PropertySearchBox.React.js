/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'

import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import NumberUtils from '../utils/NumberUtils'

import * as propertySearchActions from '../actions/PropertySearchActions'
import * as apartmentActions from '../actions/ApartmentActions'

class PropertySearchBox extends Component {
    constructor(props) {
        super(props)

        this.handleSubmit = this.handleSubmit.bind(this)
        this.onPlaceChange = this.onPlaceChange.bind(this)
        this.onSearchClicked = this.onSearchClicked.bind(this)
        this.changeMinPrice = this.changeMinPrice.bind(this)
        this.changeMaxPrice = this.changeMaxPrice.bind(this)
        this.changeMinRooms = this.changeMinRooms.bind(this)
        this.changeMaxRooms = this.changeMaxRooms.bind(this)
        this.changeTypez = this.changeTypez.bind(this)
        this.state = {blabla: ''}

    }

    changeMinPrice(e) {
        const value = e.target.value
        const {changeMinPrice } = this.props.propertySearchActions
        changeMinPrice(value)
    }

    changeMaxPrice(e) {
        const value = e.target.value
        const {changeMaxPrice } = this.props.propertySearchActions
        changeMaxPrice(value)
    }

    changeMinRooms(e) {
        const value = e.target.value
        const {changeMinRooms } = this.props.propertySearchActions
        changeMinRooms(value)
    }

    changeMaxRooms(e) {
        const value = e.target.value
        const {changeMaxRooms } = this.props.propertySearchActions
        changeMaxRooms(value)
    }

    changeTypez(e) {
        console.log('change type', e)

    }

    componentDidMount() {
        console.log('Autocomplete component',this._autocompleteInput);

        const autocompleteConfigs = {};
        const _self = this;
        this.autocomplete = new google.maps.places.Autocomplete(this._autocompleteInput, autocompleteConfigs);
        this.autocomplete.addListener('place_changed', () => {
            const place = _self.autocomplete.getPlace();
            console.log('Changed to place', place);
            _self.onPlaceChange(place)
        })
        // this._autocompleteInput.value = "Москва"
    }


    onPlaceChange(place) {
        const { changeGeometry } = this.props.propertySearchActions

        const geometry = place.geometry
        if(geometry.viewport) {
            //search within some area
            const bounds = geometry.viewport
            const ne = bounds.getNorthEast();
            const sw = bounds.getSouthWest();
            console.log('ne lat, lng:', ne.lat(), ',', ne.lng())
            console.log('sw lat, lng:', sw.lat(), ',', sw.lng())
            changeGeometry(
                {
                    bounds: {
                        ne: {
                            lat: ne.lat(),
                            lng: ne.lng()
                        },
                        sw: {
                            lat: sw.lat(),
                            lng: sw.lng()
                        }
                    }
                })
        } else {
            //search nearest to coordinates
            const location = geometry.location
        }
    }

    onSearchClicked() {
        const { clearApartmentSearch } = this.props.apartmentActions
        const propertySearch = this.props.PropertySearch
        clearApartmentSearch(propertySearch)
    }

    handleSubmit(e) {
        e.preventDefault() //avoid form submission to server
        console.log('Search')
        this.onSearchClicked()
    }

    metroDom() {
        return (<div className="col-xs-12 col-sm-4 col-md-3 form-group">


            <select name="metro" id="metro"
                    className="form-control chosen-select">
                <option value="">Метро</option>


                <option value="42">Авиамоторная</option>


                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>
                <option value="99">Октябрьская</option>

            </select>
        </div>)
    }

    render() {
        const propertySearch = this.props.PropertySearch

        console.log('property search', propertySearch)

        return (
            <form className="property-search-form border-box"
                  action="http://demo.themetrail.com/realty/property-map-vertical/"
                  onSubmit={this.handleSubmit}
            >

                <div className="row">


                    <div className="col-xs-12 col-sm-4 col-md-3 form-group select">
                        <input type="text" name="location" id="location"
                               ref={(c) => this._autocompleteInput = c}
                               placeholder="Введите Адрес/метро/улицу" className="form-control"/>
                    </div>





                    <div className="col-xs-12 col-sm-4 col-md-3 form-group select">
                        {/*TODO: bind to on change native dom event because this element is transformed to something blabla*/}
                        <select id="aaasdadasds" className="form-control chosen-select" onChange={this.changeTypez} value={this.state.blabla}>
                            <option key={''} value={''}>{''}</option>
                            <option key={'RENTER'} value={'RENTER'}>{'RENTER'}</option>
                            <option key={'LESSOR'} value={'LESSOR'}>{'LESSOR'}</option>
                        </select>
                    </div>
                    <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                        <input type="number" className="form-control chosen-select" min="1" max="3" placeholder="Комнат мин"
                               onChange={this.changeMinRooms}
                               value={(propertySearch.minRooms ? propertySearch.minRooms : '')}/>
                    </div>

                    <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                        <input type="number" className="form-control chosen-select" min="1" max="3" placeholder="Комнат макс"
                               onChange={this.changeMaxRooms}
                               value={(propertySearch.maxRooms ? propertySearch.maxRooms : '')}/>
                    </div>

                    <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                        <input type="text" className="form-control chosen-select"
                               onChange={this.changeMinPrice}
                               placeholder="Цена мин"
                               value={(propertySearch.minPrice ? NumberUtils.formatNumber(propertySearch.minPrice) : '')}/>
                    </div>

                    <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                        <input type="text" className="form-control chosen-select"
                               onChange={this.changeMaxPrice}
                               placeholder="Цена макс"
                               value={(propertySearch.maxPrice ? NumberUtils.formatNumber(propertySearch.maxPrice) : '')}/>
                    </div>

                    <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                        <input type="submit" value="Найти"
                               className="btn btn-primary btn-block form-control"/>
                    </div>


                </div>


                <input type="hidden" name="pageid" value="1149"/>


            </form>
        )
    }
}

function mapStateToProps(state) {
    return {
        PropertySearch: state.PropertySearch
    }
}

function mapDispatchToProps(dispatch) {
    console.log('mapping dispatch to props')
    return {
        propertySearchActions: bindActionCreators(propertySearchActions, dispatch),
        apartmentActions: bindActionCreators(apartmentActions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(PropertySearchBox)
