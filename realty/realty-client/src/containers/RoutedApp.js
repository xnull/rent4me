import React, {Component} from 'react'

import NavLink from '../components/NavLink'

export default class RoutedApp extends Component {

  render() {
    return (
      <div className='container'>
        <h1>App</h1>
        <ul>
          <li><NavLink to='/' onlyActiveOnIndex={true}>Home</NavLink></li>
          <li><NavLink to='/admin' >Admin</NavLink></li>
          <li><NavLink to='/login' >Login</NavLink></li>
          <li><NavLink to='/list' >List</NavLink></li>
        </ul>
        {this.props.children}
      </div>
    )
  }
}
