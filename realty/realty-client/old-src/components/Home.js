import React, { Component, PropTypes } from 'react'
import { browserHistory } from 'react-router'

export default class Home extends Component {
  constructor(props) {
    super(props)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  componentDidMount() {
    this.context.router.setRouteLeaveHook(this.props.route, this.routerWillLeave)
  }

  routerWillLeave() {
    //if(!window.confirm('Are aou sure you want to leave?')) return false
    return 'Are you sure'
  }

  handleSubmit(e) {
    e.preventDefault()

    const value = e.target.elements[0].value.toLowerCase()
    this.context.router.push(`/genre/${value}`)
    //browserHistory.push(`/genre/${value}`)
  }

  render() {
    return (
      <div className="row">
        <div className='col-md-12'>Category /</div>
        <form className='com-md-4' onSubmit={this.handleSubmit}>
          <input type='text' placeholder='genreName'/>
          <button type='submit'>Go</button>
        </form>
      </div>
    )
  }
}

Home.contextTypes = {
  router: PropTypes.object.isRequired
}
