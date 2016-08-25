import React from 'react';
import ReactDOM from 'react-dom';

//import { routes } from './routes'

import Content from './components/Content.React'
import Header from './components/Header.React'
import Footer from './components/Footer.React'

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
  <Header />,
  document.getElementById('topblabla')
);

ReactDOM.render(
  <Content />,
  document.getElementById('contentblabla')
);

ReactDOM.render(
  <Footer />,
  document.getElementById('bottomblabla')
);
