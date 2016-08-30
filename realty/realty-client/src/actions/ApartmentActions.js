/**
 * Created by dionis on 28/08/16.
 */
import {
    APARTMENT_SAVE_DETAILS,
    APARTMENT_LOAD_LIST_PART,
    APARTMENT_LOAD_DETAILS,
    APARTMENT_LIST_CLEAR_LOADING
} from '../constants/ApartmentConstants'

import 'whatwg-fetch';

var cache = new Map()
var listCache = []

export function clearApartmentSearch(params = {}) {
    console.log('Search with params', params)

    return (dispatch) => {
        dispatch({
            type: APARTMENT_LIST_CLEAR_LOADING
        })

        const newId = () => new Date().getTime() + ""

        const hasParams = Object.getOwnPropertyNames(params).length > 0;
        if(hasParams) {

            // var urlParams = "text=&with_subway=false&type=LESSOR&"
            var urlParams = "text=&with_subway=false&type=RENTER&limit=100&offset=0&"

            if(params.geometry && params.geometry.bounds) {
                const bounds = params.geometry.bounds
                const ne = bounds.ne
                const sw = bounds.sw

                urlParams += "lat_lo="+sw.lat+"&lng_lo="+sw.lng+"&lat_hi="+ne.lat+"&lng_hi="+ne.lng
            }

            fetch('http://rent4.me/rest/apartments/search?'+urlParams)
                .then(function (response) {
                    console.log('then')
                    return response.json()
                })
                .then(function (json) {
                    dispatch({
                        type: APARTMENT_LOAD_LIST_PART,
                        payload: json
                    })
                    console.log('parsed json', json)
                }).catch(function (ex) {
                console.log('parsing failed', ex)
            });
        }
    }
}
