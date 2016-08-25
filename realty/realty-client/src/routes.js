import React from 'react'

import { Router, Route, IndexRoute, IndexRedirect, Redirect } from 'react-router'
import App from './containers/App.React'

import Main from './components/Main.React'
import NotFound from './components/NotFound.React'

export const routes = (
  <div>
    <Route path='/' component={App}>
      <IndexRoute component={Main}/>
      <Redirect from='/old_admin' to='/' />
      <Route path='*' component={NotFound} />
    </Route>

  </div>
)
