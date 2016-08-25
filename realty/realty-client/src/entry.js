import React from 'react';
import ReactDOM from 'react-dom';

//import { routes } from './routes'

import PropertyPreviewMainList from './components/PropertyPreviewMainList.React'
import PropertySearchBox from './components/PropertySearchBox.React'
import Header from './components/Header.React'
import Menu from './components/Menu.React'

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
        id: "1",
        address: "Bla bla street",
        description: "Lorem ipsum",
        rented: true,
        previewImage: 'http://unsplash.it/600/300?image=1081'
    },

    {
        id: "2",
        address: "Ololo",
        description: "Ararara",
        rented: false,
        previewImage: 'http://unsplash.it/600/300?image=1080'
    },

    {
        id: "3",
        address: "Bla bla street asdasd",
        description: "Lorem ipsum",
        rented: true,
        previewImage: 'http://unsplash.it/600/300?image=1079'
    },

    {
        id: "4",
        address: "Hahahah",
        description: "Ararara",
        rented: false,
        previewImage: 'http://unsplash.it/600/300?image=1078'
    },

    {
        id: "5",
        address: "Elm street",
        description: "Lorem ipsum",
        rented: true,
        previewImage: 'http://unsplash.it/600/300?image=1077'
    },

    {
        id: "6",
        address: "Chichuahua",
        description: "Ararara",
        rented: false,
        previewImage: 'http://unsplash.it/600/300'
    }
]


ReactDOM.render(
  <Header />,
  document.getElementById('topblabla')
);

ReactDOM.render(
  <div>
    <PropertySearchBox />
    <PropertyPreviewMainList apartments={fakeData}/>
  </div>,
  document.getElementById('asdasdasdsasd')
);
