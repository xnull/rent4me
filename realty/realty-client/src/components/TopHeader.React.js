/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'

export default class TopHeader extends Component {
    render() {

        return (
          <div className="top-header">
              <div className="container">
                  <div className="top-header-sidebar">
                      <div className="textwidget"><a href="tel:00155522668890">+1 555 22 66 8890</a> · <a
                              href="mailto:info@yourcompany.com">info@yourcompany.com</a></div>
                  </div>

                  <div className="top-header-links primary-tooltips">


                      <a href="http://demo.themetrail.com/realty/property-submit/" data-toggle="modal">Submit Property</a>

                      <a href="#login-modal" data-toggle="modal">Login</a>

                  </div>
              </div>
          </div>
        )
    }
}
