import React, { Component } from 'react'

export default class Admin extends Component {
  static onEnter(nextState, replace) {
    const login = window.localStorage.getItem('rr_login')
    if(login == 'admin') {
      console.log('let hem in!')
    } else {
      console.log('go away!')
      replace('/')
    }
  }

  render() {
    return (
      <div className="row">
        <div className='col-md-12'>Category /admin</div>
      </div>
    )
  }
}
