import React from 'react';
import ReactDOM from 'react-dom';

//import { routes } from './routes'

import PropertyPreview from './components/PropertyPreview.React'

import { Router, Route, IndexRoute, browserHistory, hashHistory } from 'react-router'

// import configureStore from './store/configureStore.js'

// const store = configureStore();

// ReactDOM.render(
//   <Provider store={store}>
//     <App />
//   </Provider>,
//   document.getElementById('example')
// );

const fakeData = [
    {
        address: "Bla bla street",
        description: "Lorem ipsum",
        rented: true,
        previewImage: 'http://demo.themetrail.com/realty/wp-content/uploads/2015/10/realty-property-one-story-house-2-600x300.jpg'
    }
]


ReactDOM.render(
  <PropertyPreview apartment={fakeData[0]}/>,
  document.getElementById('asdasdasdsasd')
);
