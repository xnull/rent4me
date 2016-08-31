/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import {Link} from 'react-router'

export default class SmartLink extends Component {
    render() {
        const toLink = this.props.to || ''
        if(toLink.startsWith('http://') || toLink.startsWith('https://')) {
            return (<a href={toLink} target="_blank">{this.props.children}</a> );
        } else {
            return (<Link {...this.props}/>)
        }
    }
}

SmartLink.propTypes = {
    to: PropTypes.string.isRequired
}