import React, {Component} from 'react'

import Header from '../components/Header.React'
import Footer from '../components/Footer.React'

export default class App extends Component {
    constructor(props) {
        super(props)
    }

    render() {
        return (
            <div>
                <Header />
                {this.props.children}
                <Footer />
            </div>
        )
    }
}