import React, { Component } from 'react'
import { Link } from 'react-router'

export default class List extends Component {
  render() {
    return (
      <div>
        <div className="row">
          <div className='col-md-12'>
            <h3>Genre list</h3>
          </div>
        </div>

        <div className="row">
          <div className='col-md-12'>
            <ul>
              <li>
                <Link to='/genre/house/'>House</Link>
              </li>
              <li>
                <Link to='/genre/dnb/'>Drumnbass</Link>
              </li>
              <li>
                <Link to='/genre/hip-hop/'>Hiphop</Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    )
  }
}
