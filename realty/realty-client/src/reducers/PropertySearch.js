import {
    PROPERTY_SEARCH_GEOMETRY_CHANGED
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
        default:
            return state
    }
}
