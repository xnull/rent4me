import {combineReducers} from 'redux'
import {routerReducer} from 'react-router-redux'

import Apartments from './Apartments'
import PropertySearch from './PropertySearch'

const reducers = {Apartments, PropertySearch ,routing: routerReducer}

console.log('reducers', reducers)

export default combineReducers(reducers)
