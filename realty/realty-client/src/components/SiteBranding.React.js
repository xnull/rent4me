/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import Menu from './Menu.React'
import {Link} from 'react-router'

export default class SiteBranding extends Component {
    render() {

        return (
            <div className="container">
                <div className="site-branding">

                    <p className="site-title">
                        <Link to="/">
                            <img
                                 src="/images/rent4me-icon.png"
                                 className="site-logo"
                                 alt="logo"
                                 srcSet="/images/rent4me-icon.png 2x"/>
                        </Link>
                    </p>

                    <a id="toggle-navigation" className="navbar-togglex" href="#"><i></i></a>
                    <div className="mobile-menu-overlay hide"></div>
                </div>

                <Menu />
            </div>
        )
    }
}
