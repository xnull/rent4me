/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import Menu from './Menu.React'

export default class SiteBranding extends Component {
    render() {

        return (
          <div className="container">
              <div className="site-branding">

                  <p className="site-title"><a href="http://demo.themetrail.com/realty/" rel="home"><img width="124" height="23"
                                                                                                     src="http://demo.themetrail.com/realty/wp-content/uploads/2015/10/logo.png"
                                                                                                     className="site-logo"
                                                                                                     alt="logo"
                                                                                                     srcSet="http://demo.themetrail.com/realty/wp-content/uploads/2015/10/logo@2x.png 2x"/></a>
                  </p>

                  <a id="toggle-navigation" className="navbar-togglex" href="#"><i></i></a>
                  <div className="mobile-menu-overlay hide"></div>
              </div>

              <Menu />
          </div>
        )
    }
}
