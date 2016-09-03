/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

import PropertySearchBox from '../components/PropertySearchBox.React'
import PropertyPreviewMainList from '../components/PropertyPreviewMainList.React'

import * as apartmentActions from '../actions/ApartmentActions'

class ApartmentSearch extends Component {
    constructor(props) {
        super(props)

        this.loadNextPageOfApartments = this.loadNextPageOfApartments.bind(this)
    }

    loadNextPageOfApartments() {
        //console.log('Loading apartments')
        const Apartments = this.props.Apartments;
        const propertySearch = this.props.PropertySearch;

        const {nextPageSearch} = this.props.apartmentActions;
        nextPageSearch(propertySearch,Apartments.apartments.length)
    }

    render() {
        const Apartments = this.props.Apartments;

        //console.log("Apartments:", Apartments.apartments)
        const apartments = Apartments.apartments

        return (
            <article id="post-1149" className="post-1149 page type-page status-publish hentry">


                <div className="entry-content">

                    <div className="full-width">
                        <div className="vc_row vc_row-fluid vc_custom_1466053722682 full-width">
                            <div className="wpb_column vc_column_container vc_col-sm-12">
                                <div className="vc_column-inner ">
                                    <div className="wpb_wrapper">
                                        <div style={{width: '100%', height: '100px'}}></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="container">
                        <div className="vc_row vc_row-fluid vc_custom_1466053815236 boxed">
                            <div className="wpb_column vc_column_container vc_col-sm-12">
                                <div className="vc_column-inner ">
                                    <div className="wpb_wrapper">
                                        {
                                            Apartments.geoErrorInParams ? (<div>{Apartments.geoErrorInParams}</div>) : ''
                                        }
                                        
                                        <PropertySearchBox />
                                        <div id="property-items" className="property-items show-compare grid-view"
                                             data-view="grid-view">

                                            <PropertyPreviewMainList apartments={apartments}/>

                                            {
                                                (Apartments.hasNextPage || (Apartments.apartments.length > 0 && Apartments.error))
                                                    ? (
                                                    <div id="pagination">
                                                        <ul className='page-numbers'>
                                                            <li>
                                                                <a className="page-numbers" onClick={this.loadNextPageOfApartments}>
                                                                    Далее
                                                                </a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                ) : ''
                                            }
                                            {
                                                Apartments.loadInProgress
                                                    ? (
                                                    <div id="pagination">
                                                        <ul className='page-numbers'>
                                                            <li>
                                                                Загрузка...
                                                            </li>
                                                        </ul>
                                                    </div>
                                                ) : ''
                                            } {
                                                Apartments.error
                                                    ? (
                                                    <div id="pagination">
                                                        <ul className='page-numbers'>
                                                            <li>
                                                                Произошла ошибка при загрузке объявллений
                                                            </li>
                                                        </ul>
                                                    </div>
                                                ) : ''
                                            }


                                        </div>


                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                </div>

            </article>
        )
    }
}

function mapStateToProps(state) {
    return {
        Apartments: state.Apartments,
        PropertySearch: state.PropertySearch
    }
}

function mapDispatchToProps(dispatch) {
    //console.log('mapping dispatch to props')
    return {
        apartmentActions: bindActionCreators(apartmentActions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ApartmentSearch)
