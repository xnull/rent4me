/**
 * Гугловая строка автокомплита адреса
 * https://developers.google.com/places/?hl=ru
 * https://developers.google.com/maps/documentation/javascript/examples/places-autocomplete?hl=ru
 *
 * Created by null on 07.01.15.
 */

var React = require('react');

var SearchAddress = React.createClass({
    componentDidMount: function (rootNode) {
        var autocomplete = new google.maps.places.Autocomplete(this.refs.googleSearchBox.getDOMNode());
    },

    render: function () {
        return (
            <input
                id="searchbox"
                className="form-control"
                type="text"
                placeholder="Введите местоположение"
                onChange={this.onAddressChange}
                ref="googleSearchBox"
            />
        )
    },

    onAddressChange: function (event) {
        //this.props.onChange(event.target.value);
    }
});

module.exports = SearchAddress;