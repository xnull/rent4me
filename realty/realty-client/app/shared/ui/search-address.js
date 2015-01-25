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
        /*
         Показывать только города
         var options = {
         types: ['(cities)'],
         componentRestrictions: {country: "us"}
         };*/
        var autocomplete = new google.maps.places.Autocomplete(this.refs.googleSearchBox.getDOMNode());
    },
    /**
     * Проверка корректности параметров компонента.
     */
    propTypes: {
        uiSize: React.PropTypes.number.isRequired,
        uiLabelSize: React.PropTypes.number.isRequired
    },

    /**
     * Настроки UI
     * @returns {{size: string, labelSize: string, form: string}}
     */
    uiProps: function () {
        var frmSize = 12 - parseInt(this.props.uiLabelSize);
        return {
            size: 'col-md-' + this.props.uiSize,
            labelSize: 'col-md-' + this.props.uiLabelSize,
            form: 'col-md-' + frmSize
        }
    },

    render: function () {
        var uiProps = this.uiProps();
        return (
            <div className={uiProps.size}>
                <div className={uiProps.labelSize}>
                    <label className="control-label pull-right"></label>
                </div>

                <div className={uiProps.form}>
                    <input
                        id="searchbox"
                        className="form-control"
                        type="text"
                        placeholder="Адрес, улица, метро, район..."
                        onChange={this.onAddressChange}
                        ref="googleSearchBox"
                    />
                </div>
            </div>
        )
    },

    onAddressChange: function (event) {
        //this.props.onChange(event.target.value);
    }
});

module.exports = SearchAddress;