import { combineReducers } from 'redux'
import { routerReducer } from 'react-router-redux'

import Apartment from './Apartment'

export default combineReducers({
  Apartment, routing: routerReducer
})
