/**
 * Created by dionis on 28/08/16.
 */
import {APARTMENT_SAVE_DETAILS, APARTMENT_LOAD_LIST_PART, APARTMENT_LOAD_DETAILS, APARTMENT_LIST_CLEAR_LOADING} from '../constants/ApartmentConstants'

import 'whatwg-fetch';

var cache = new Map()
var listCache = []

export function clearApartmentSearch() {
    return (dispatch) => {
        dispatch({
            type: APARTMENT_LIST_CLEAR_LOADING
        })

        fetch('http://rent4.me/rest/cities/search?lng=24.6817121&lat=59.397131900000005')
            .then(function (response) {
                console.log('then')
                return response.json()
            })
            .then(function (json) {
                var id = new Date().getTime()+""
                dispatch({
                    type: APARTMENT_LOAD_LIST_PART,
                    payload: [{
                        id: id,
                        address: "Bla bla street "+id,
                        description: "Lorem ipsum",
                        rented: true,
                        previewImage: 'http://unsplash.it/600/300?image=1081'
                    }]
                })
                console.log('parsed json', json)
            }).catch(function (ex) {
                console.log('parsing failed', ex)
            });
    }
}
