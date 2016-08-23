import React, { PropTypes, Component } from 'react'

export default class User extends Component {
  render() {
    const { name, error } = this.props

    const template = name
     ? (<p>Hello, {name}!</p>)
     : (<button className='btn' onClick={this.props.handleLogin}>Login</button>)

    return (
      <div>
        {template}
        {error ? (<p className='error'> {error} <br/> Please try again... </p>) : '' }
      </div>
    )
  }
}

User.propTypes = {
  name: PropTypes.string.isRequired,
  handleLogin: PropTypes.func.isRequired,
  error: PropTypes.string.isRequired
}
