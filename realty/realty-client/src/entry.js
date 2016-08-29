import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux'

import {routes} from './routes'

import configureStore from './stores/configureStore'

import { applyRouterMiddleware, Router, Route, IndexRoute, browserHistory, hashHistory} from 'react-router'
import {syncHistoryWithStore} from 'react-router-redux'
import { useScroll } from 'react-router-scroll'


// import configureStore from './store/configureStore.js'

// const store = configureStore();

// ReactDOM.render(
//   <Provider store={store}>
//     <App />
//   </Provider>,
//   document.getElementById('example')
// );

const store = configureStore({})

const history = syncHistoryWithStore(browserHistory, store)

ReactDOM.render(
    <Provider store={store}>
        <Router history={history} routes={routes} render={applyRouterMiddleware(useScroll())}/>
    </Provider>,
    document.getElementById('app_container')
);
