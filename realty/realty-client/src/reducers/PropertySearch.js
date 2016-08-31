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

const initialState = {
    roomMin: null,
    roomMax: null,
    geometry: {
        bounds: null,
        latLng: null
    },
    minPrice: null,
    maxPrice: null,
    error: ''
};

export default function PropertySearch(state = initialState, action) {
    console.log('page reducer, state:', action.type, state, action.payload)
    switch (action.type) {
        case PROPERTY_SEARCH_GEOMETRY_CHANGED:
            return {...state, geometry: action.payload }
        case PROPERTY_SEARCH_MIN_PRICE_CHANGED:
            return {...state, minPrice: action.payload }
        default:
            return state
    }
}
