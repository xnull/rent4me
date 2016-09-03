/**
 * Created by dionis on 28/08/16.
 */
import {
    APARTMENT_SAVE_DETAILS,
    APARTMENT_LOAD_LIST_PART,
    APARTMENT_LOAD_DETAILS,
    APARTMENT_LIST_CLEAR_LOADING,
    APARTMENT_LIST_NEXT_PAGE_LOADING,
    APARTMENT_ERROR_LOADING,
    APARTMENT_SEARCH_INPUT_GEO_ERROR,
} from '../constants/ApartmentConstants'

import 'whatwg-fetch';
import URLUtils from '../utils/URLUtils'

function appendToQueryParamsIfNotEmpty(queryParams, params, targetKey, paramKey) {
    if(params[paramKey]) {
        queryParams[targetKey] = params[paramKey]
    }
}

function canProceed(params) {
    let canProceed = false
    if (params.geometry) {
        if (params.geometry.bounds) {
            canProceed = true
        }

        if(params.geometry.location){
            canProceed = true
        }
    }

    return canProceed
}

function doLoading(dispatch, params, limit, offset) {
    const hasParams = Object.getOwnPropertyNames(params).length > 0;
    if(hasParams) {

        let queryParams = {}
        queryParams['with_subway'] = false
        queryParams['limit'] = limit
        queryParams['offset'] = offset
        queryParams['type'] = params['type']
        queryParams['text'] = params['text'] || '' //this param is mandatory server side - should relax it

        appendToQueryParamsIfNotEmpty(queryParams, params, 'min_price', 'minPrice')
        appendToQueryParamsIfNotEmpty(queryParams, params, 'max_price', 'maxPrice')

        let minRooms = params['minRooms'] || null
        let maxRooms = params['maxRooms'] || null
        if(minRooms || maxRooms) {
            minRooms = minRooms || 1
            maxRooms = maxRooms || 3
            var roomsArray = []
            for(var i = minRooms; i <= maxRooms; i++) {
                roomsArray.push(i);
            }
            queryParams['rooms'] = roomsArray
        }

        let canProceed = false
        if (params.geometry) {
            if (params.geometry.bounds) {
                const bounds = params.geometry.bounds
                const ne = bounds.ne
                const sw = bounds.sw

                queryParams['lat_lo'] = sw.lat
                queryParams['lng_lo'] = sw.lng
                queryParams['lat_hi'] = ne.lat
                queryParams['lng_hi'] = ne.lng
                canProceed = true
            }

            if(params.geometry.location){
                const location = params.geometry.location
                queryParams['lat'] = location.lat
                queryParams['lng'] = location.lng
                canProceed = true
            }
        }

        if(!canProceed) {
            return
        }

        var url =
            URLUtils.buildUrl('//rent4.me', '/rest/apartments/search', queryParams);

        // console.log('url: ', url)

        fetch(url)
            .then(function (response) {
                //console.log('then')
                return response.json()
            })
            .then(function (json) {
                dispatch({
                    type: APARTMENT_LOAD_LIST_PART,
                    payload: json
                })
                //console.log('parsed json', json)
            }).catch(function (ex) {
                //console.log('parsing failed', ex)
                dispatch({
                    type: APARTMENT_ERROR_LOADING
                })
            });
    }
}

export function clearApartmentSearch(params = {}) {
    return (dispatch) => {
        if(!canProceed(params)) {
            dispatch({
                type: APARTMENT_SEARCH_INPUT_GEO_ERROR
            })
            return
        }

        dispatch({
            type: APARTMENT_LIST_CLEAR_LOADING
        })

        doLoading(dispatch, params, 100, 0)
    }
}
export function nextPageSearch(params = {}, offset) {
    if(!canProceed(params)) {
        dispatch({
            type: APARTMENT_SEARCH_INPUT_GEO_ERROR
        })
        return
    }

    return (dispatch) => {
        dispatch({
            type: APARTMENT_LIST_NEXT_PAGE_LOADING
        })

        doLoading(dispatch, params, 100, offset)
    }
}
