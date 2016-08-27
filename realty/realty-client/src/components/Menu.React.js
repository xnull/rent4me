/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import { Link } from 'react-router'

export default class Menu extends Component {
    render() {

        const menu = [
          {title: 'Главная', to: '/', children: []}
        ]

        return (
          <nav className="main-navigation" id="navigation">
              <ul id="menu-main-menu" className="primary-menu">
                  <li className="menu-item">
                      <Link to="/">Home</Link>
                  </li>
                  <li className="menu-item">
                    <Link to="/contact">Contact</Link>
                  </li>
              </ul>
          </nav>
        )
    }
}
