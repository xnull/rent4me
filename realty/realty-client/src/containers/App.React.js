import React, {Component} from 'react'

import Header from '../components/Header.React'
import Footer from '../components/Footer.React'

export default class App extends Component {

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
