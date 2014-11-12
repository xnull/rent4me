/** @jsx React.DOM */

var CommentBox = React.createClass({
    displayName: 'CommentBox',
    render: function () {
        return (
            <div className="commentBox">
            Hello, world! I am a CommentBox.
            </div>
        );
    }
});

React.render(
    <CommentBox/>,
    document.getElementById('content')
);