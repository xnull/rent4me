import React from 'react'

import {Router, Route, IndexRoute, IndexRedirect, Redirect} from 'react-router'
import App from './containers/App.React'

import ApartmentSearch from './containers/ApartmentSearch.React'
import NotFound from './components/NotFound.React'

export const routes = (
    <div>
        <Route path='/' component={App}>
            <IndexRoute component={ApartmentSearch}/>
            {/*redirect from old urls that were on google to main site page*/}
            <Redirect from='/personal' to='/'/>
            <Route path='*' component={NotFound}/>
        </Route>

    </div>
)
