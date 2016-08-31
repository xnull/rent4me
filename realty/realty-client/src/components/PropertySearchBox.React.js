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

    }

    changeMinPrice(e) {
        const value = e.target.value
        const {changeMinPrice } = this.props.propertySearchActions
        changeMinPrice(value)
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

    render() {
        const propertySearch = this.props.PropertySearch

        console.log('property search', propertySearch)

        return (
            <form className="property-search-form border-box"
                  action="http://demo.themetrail.com/realty/property-map-vertical/"
                  onSubmit={this.handleSubmit}
            >

                <div className="row">


                    <div className="col-xs-12 col-sm-8 col-md-6 form-group select">
                        <input type="text" name="location" id="location"
                               ref={(c) => this._autocompleteInput = c}
                               placeholder="Введите Адрес/метро/улицу" className="form-control"/>
                    </div>



                    <div className="col-xs-12 col-sm-4 col-md-3 form-group">


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


                    </div>

                    <div className="col-xs-12 col-sm-4 col-md-3 form-group select">
                        <select name="status" id="status" className="form-control chosen-select" defaultValue="renter">
                            <option value="renter">
                                Я хочу Снять
                            </option>
                            <option value="lessor">
                                Я хочу Сдать
                            </option>
                        </select>
                    </div>
                    <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                        <input type="number" name="minrooms" id="minrooms" className="form-control chosen-select" min="1" max="3" placeholder="Комнат мин"/>
                    </div>

                    <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                        <input type="number" name="maxrooms" id="maxrooms" className="form-control chosen-select" min="1" max="3" placeholder="Комнат макс"/>
                    </div>

                    <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                        <input type="text" name="minprice" id="minprice" className="form-control chosen-select"
                               onChange={this.changeMinPrice}
                               placeholder="Цена мин" value={(propertySearch.minPrice ? NumberUtils.formatNumber(propertySearch.minPrice) : null)}/>
                    </div>

                    <div className="col-xs-12 col-sm-4 col-md-3 form-group">
                        <input type="number" name="maxprice" id="maxprice" className="form-control chosen-select" min="1" max="3" placeholder="Цена макс"/>
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
