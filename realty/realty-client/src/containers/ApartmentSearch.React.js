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

        this.initialLoadApartments = this.initialLoadApartments.bind(this)
    }

    initialLoadApartments() {
        console.log('Loading apartments')
        const Apartments = this.props.Apartments;
        if(Apartments.apartments.length == 0) {
            const {clearApartmentSearch} = this.props.apartmentActions;
            clearApartmentSearch()
        }
    }

    componentDidMount() {
        this.initialLoadApartments()
    }

    render() {
        const Apartments = this.props.Apartments;

        console.log("Apartments:", Apartments.apartments)
        const apartments = Apartments.apartments
        /*
         const fakeData = [
         {
         id: "1",
         address: "Bla bla street",
         description: "Lorem ipsum",
         rented: true,
         previewImage: 'http://unsplash.it/600/300?image=1081'
         },

         {
         id: "2",
         address: "Ololo",
         description: "Ararara",
         rented: false,
         previewImage: 'http://unsplash.it/600/300?image=1080'
         },

         {
         id: "3",
         address: "Bla bla street asdasd",
         description: "Lorem ipsum",
         rented: true,
         previewImage: 'http://unsplash.it/600/300?image=1079'
         },

         {
         id: "4",
         address: "Hahahah",
         description: "Ararara",
         rented: false,
         previewImage: 'http://unsplash.it/600/300?image=1078'
         },

         {
         id: "5",
         address: "Elm street",
         description: "Lorem ipsum",
         rented: true,
         previewImage: 'http://unsplash.it/600/300?image=1077'
         },

         {
         id: "6",
         address: "Chichuahua",
         description: "Ararara",
         rented: false,
         previewImage: 'http://unsplash.it/600/300'
         }
         ];
         */
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
                                        <PropertySearchBox />
                                        <div id="property-items" className="property-items show-compare grid-view"
                                             data-view="grid-view">

                                            <PropertyPreviewMainList apartments={apartments}/>

                                            <div id="pagination">
                                                <ul className='page-numbers'>
                                                    <li>
                                                        <a className="page-numbers">
                                                            Далее
                                                        </a>
                                                    </li>
                                                </ul>
                                            </div>


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
        Apartments: state.Apartments
    }
}

function mapDispatchToProps(dispatch) {
    console.log('mapping dispatch to props')
    return {
        apartmentActions: bindActionCreators(apartmentActions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ApartmentSearch)
