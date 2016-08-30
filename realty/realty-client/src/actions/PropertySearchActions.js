/**
 * Created by dionis on 28/08/16.
 */
import {
    PROPERTY_SEARCH_GEOMETRY_CHANGED
} from '../constants/PropertySearchConstants'

export function changeGeometry(geometry) {
    return (dispatch) => {
        dispatch({
            type: PROPERTY_SEARCH_GEOMETRY_CHANGED,
            payload: geometry
        })
    }
}
