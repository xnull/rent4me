import React, { Component } from 'react'
import { Link } from 'react-router'

export default class Admin extends Component {
  render() {
    return (
      <div className="row">
        <h3>Not found</h3>
        <div className='col-md-12'>Back to <Link to='#/'>home</Link></div>
      </div>
    )
  }
}
