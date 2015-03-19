var React = require('react');

var AddressBox = React.createClass({

    propTypes: {
        onAddressChange: React.PropTypes.func.isRequired,
        displayValue: React.PropTypes.string.isRequired
    },

    render: function () {
        return (
            <input
                id="addressInput"
                className="form-control"
                type="text"
                value={this.props.displayValue}
                style={{
                    paddingTop: 0,
                    paddingBottom: 0,
                    borderRadius: 0
                }}
                placeholder="Введите местоположение"
                onChange={this.props.onAddressChange}
            />
        )
    }
});

module.exports = AddressBox;