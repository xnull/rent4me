import React from 'react';
import ReactDOM from 'react-dom';

import { routes } from './routes'

import { Router, Route, IndexRoute, browserHistory, hashHistory } from 'react-router'

// import configureStore from './store/configureStore.js'

// const store = configureStore();

// ReactDOM.render(
//   <Provider store={store}>
//     <App />
//   </Provider>,
//   document.getElementById('example')
// );

ReactDOM.render(
  <Router history={browserHistory} routes={routes} />,
  document.getElementById('app_container')
);
