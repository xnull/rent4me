/**
 * Created by dionis on 28/08/16.
 */
import {
    PROPERTY_SEARCH_GEOMETRY_CHANGED,
    PROPERTY_SEARCH_MIN_PRICE_CHANGED,
    PROPERTY_SEARCH_MAX_PRICE_CHANGED,
    PROPERTY_SEARCH_METRO_ADDED,
    PROPERTY_SEARCH_METRO_REMOVED,
    PROPERTY_SEARCH_MIN_ROOMS_CHANGED,
    PROPERTY_SEARCH_MAX_ROOMS_CHANGED,
    PROPERTY_SEARCH_TYPE_CHANGED,
    PROPERTY_SEARCH_TEXT_CHANGED,
    PROPERTY_SEARCH_TEXT_REMOVED,
} from '../constants/PropertySearchConstants'

import NumberUtils from '../utils/NumberUtils'

export function changeGeometry(geometry) {
    return (dispatch) => {
        dispatch({
            type: PROPERTY_SEARCH_GEOMETRY_CHANGED,
            payload: geometry
        })
    }
}

export function changeMinPrice(price) {
    return (dispatch) => {
        dispatch({
            type: PROPERTY_SEARCH_MIN_PRICE_CHANGED,
            payload: NumberUtils.parseNumber(price)
        })
    }
}

export function changeMaxPrice(price) {
    return (dispatch) => {
        price = NumberUtils.parseNumber(price)
        dispatch({
            type: PROPERTY_SEARCH_MAX_PRICE_CHANGED,
            payload: NumberUtils.parseNumber(price)
        })
    }
}

export function changeMinRooms(rooms) {
    return (dispatch) => {
        dispatch({
            type: PROPERTY_SEARCH_MIN_ROOMS_CHANGED,
            payload: NumberUtils.numBetween(NumberUtils.parseNumber(rooms), 1, 3)
        })
    }
}

export function changeMaxRooms(rooms) {
    return (dispatch) => {
        dispatch({
            type: PROPERTY_SEARCH_MAX_ROOMS_CHANGED,
            payload: NumberUtils.numBetween(NumberUtils.parseNumber(rooms), 1, 3)
        })
    }
}

export function changeMetroAdded(metroInfo) {
    return (dispatch) => {
        dispatch({
            type: PROPERTY_SEARCH_METRO_ADDED,
            payload: metroInfo
        })
    }
}

export function changeMetroRemoved(metroInfo) {
    return (dispatch) => {
        dispatch({
            type: PROPERTY_SEARCH_METRO_REMOVED,
            payload: metroInfo
        })
    }
}

export function changeText(text) {
    return (dispatch) => {
        dispatch({
            type: PROPERTY_SEARCH_TEXT_CHANGED,
            payload: text
        })
    }
}

export function removeText() {
    return (dispatch) => {
        dispatch({
            type: PROPERTY_SEARCH_TEXT_REMOVED
        })
    }
}

export function changeType(type) {
    return (dispatch) => {
        dispatch({
            type: PROPERTY_SEARCH_TYPE_CHANGED,
            payload: type == 'LESSOR' ? 'LESSOR' : 'RENTER'
        })
    }
}
