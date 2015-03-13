/**
 * Created by null on 25.01.15.
 */
var React = require('react');

var MetroBubble = React.createClass({
    /**
     * Проверка корректности параметров компонента.
     */
    propTypes: {
        id: React.PropTypes.any.isRequired,
        displayValue: React.PropTypes.string.isRequired,
        onRemove: React.PropTypes.func.isRequired
    },

    render: function () {
        var self = this;
        var onClickFunc = (id) => {return function(){ return self.props.onRemove(id);}};

        return (
            <span className="badge badge-metro">
                {this.props.displayValue} <b className="glyphicon glyphicon-remove-circle clickable" onClick={onClickFunc(this.props.id)}></b>
            </span>
        )
    }
});

module.exports = MetroBubble;