import { combineReducers } from 'redux'
import { routerReducer } from 'react-router-redux'

import Apartments from './Apartments'

const reducers = { Apartments, routing: routerReducer }

console.log('reducers', reducers)

export default combineReducers(reducers)
