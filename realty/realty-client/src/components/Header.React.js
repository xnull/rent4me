/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import TopHeader from './TopHeader.React'
import SiteBranding from './SiteBranding.React'

export default class Header extends Component {
    render() {

        return (
            <header id="header">
                <SiteBranding />
                <TopHeader />
            </header>
        )
    }
}
