/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import {Link} from 'react-router'

export default class Menu extends Component {
    render() {
        return (
            <nav className="main-navigation" id="navigation">
                <ul id="menu-main-menu" className="primary-menu">
                    <li className="menu-item">
                        <Link to="/">На главную</Link>
                    </li>
                    {/* Temporarily disable until we have contact functionality developed
                    <li className="menu-item">
                        <Link to="/contact">Контакт</Link>
                    </li>
                    */
                    }
                </ul>
            </nav>
        )
    }
}
