/**
 *
 */

var Modal = React.createClass({
    isItTrue: function () {
        return this.props.hey
    },

    render: function () {
        return (
            <div>
                <p> {this.isItTrue()} </p>
            </div>
        )
    }
});

React.render(<Modal hey={'nigushka'}/>, document.body);
