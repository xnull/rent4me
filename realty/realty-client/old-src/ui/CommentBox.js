import React from 'react'
import CommentForm from './CommentForm.js'
import CommentList from './CommentList.js'
import 'whatwg-fetch';

module.exports = class CommentBox extends React.Component {
  constructor(props) {
    super(props)
    this.state = {data: []}
    //this.loadFromServer = this.loadFromServer.bind(this)
    this.handleCommentSubmit = this.handleCommentSubmit.bind(this)
  }

  loadFromServer() {
    var that = this;
    fetch('http://www.mocky.io/v2/57b5ff0d0f00003f14ae6fd2')
      .then(function(response) {
        return response.json()
      }).then(function(json) {
        that.setState({data: json});
      }).catch(function(ex) {
        console.log('parsing failed', ex)
      })
  }

  handleCommentSubmit(comment) {

    //let { x, y, ...z } = { x: 1, y: 2, a: 3, b: 4 };

    const oldData = this.state.data;
    //const newComment = Object.assign({}, comment, {id: new Date().getTime()})
    const newComment = { ...comment, id: new Date().getTime() }

    const newData = [...oldData, newComment]
    this.setState({data: newData})
  }

  componentDidMount() {
    this.loadFromServer()
  }

  render() {
    return (
      <div className="commentBox">
        <h1>Comments</h1>
        <CommentList data={this.state.data} />
        <CommentForm onCommentSubmit={this.handleCommentSubmit}/>
      </div>
    );
  }
}

// module.exports = CommentBox;
