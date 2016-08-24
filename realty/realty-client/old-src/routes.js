import React from 'react'

import Admin from './components/Admin'
import Genre from './components/Genre'
import Home from './components/Home'
import Login from './components/Login'
import List from './components/List'
import Release from './components/Release'
import NotFound from './components/NotFound'

import RoutedApp from './containers/RoutedApp.js'

import { Router, Route, IndexRoute, IndexRedirect, Redirect } from 'react-router'

//
// <IndexRedirect to='/list' />

export const routes = (
  <div>
    <Route path='/' component={RoutedApp}>
      <IndexRoute component={Home}/>
      <Redirect from='/old_admin' to='/admin' />
      <Route path='/admin' component={Admin} onEnter={Admin.onEnter} />
      <Route path='/login' component={Login} />
      <Route path='/genre/:genre' component={Genre} >
        <Route path="/genre/:genre/:release" component={Release} />
      </Route>
      <Route path='/list' component={List} />
    </Route>
    <Route path='*' component={NotFound} />
  </div>
)
