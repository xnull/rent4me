import React, { Component } from 'react'

export default class Genre extends Component {
  render() {
    return this.props.params.release
    ? (
      <div className="row">
        <div className='col-md-12'>Genre: {this.props.params.genre}</div>
        <div className='col-md-12'>{this.props.children}</div>
      </div>
    )
    : (
      <div className="row">
        <div className='col-md-12'>Genre: {this.props.params.genre}</div>
        <div className='col-md-12'>Releases here soon</div>
      </div>
    )
  }
}
