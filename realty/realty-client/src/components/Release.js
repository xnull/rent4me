import React, { Component } from 'react'
import { Link } from 'react-router'

export default class Release extends Component {
  render() {
    const releaseName = this.props.params.release.replace(/-/g, ' ')

    return (
          <div className='col-md-12'>
            <h3>{releaseName}</h3>
          </div>
    )
  }
}
