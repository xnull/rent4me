import React from 'react'
import Remarkable from 'remarkable'

class Comment extends React.Component {
  rawMarkup() {
    var md = new Remarkable();
    var rawMarkup = md.render(this.props.children.toString());
    return { __html: rawMarkup };
  }


  render() {
    var md = new Remarkable();

    return (
      <div className="comment">
        <h2 className="commentAuthor">
          {this.props.author}
        </h2>
        <span dangerouslySetInnerHTML={this.rawMarkup()} />
      </div>
    );
  }
}

module.exports = class CommentList extends React.Component {

  render() {
    var commentNodes = this.props.data.map((comment) =>
       (
        <Comment author={comment.author} key={comment.id}>
          {comment.text}
        </Comment>
      )
    );

    return (
      <div className="commentList">
        {commentNodes}
      </div>
    );
  }
}

// module.exports = CommentBox;
