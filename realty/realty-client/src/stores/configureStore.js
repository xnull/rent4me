import { createStore, applyMiddleware } from 'redux'
import rootReducer from '../reducers/index'
import createLogger from 'redux-logger'
import thunk from 'redux-thunk'
import { syncHistoryWithStore, routerReducer } from 'react-router-redux'

export default function configureStore(initialState) {
    const logger = createLogger()
    const store = createStore(
        rootReducer,
        initialState,
        applyMiddleware(thunk, logger)
    );
    return store
}
