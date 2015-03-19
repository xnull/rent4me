/**
 * Created by null on 07.01.15.
 */

var React = require('react');
var accounting = require('accounting');

var PriceRange = React.createClass({
    propTypes: {
        className: React.PropTypes.string.isRequired,
        uiSize: React.PropTypes.number.isRequired,
        uiLabelSize: React.PropTypes.number.isRequired,
        minPrice: React.PropTypes.number,
        maxPrice: React.PropTypes.number,
        onKeyPress: React.PropTypes.func,
        onMaxPriceChange: React.PropTypes.func.isRequired,
        onMinPriceChange: React.PropTypes.func.isRequired
    },

    /**
     * Adjust UI settings
     * @returns {{size: string, labelSize: string, form: string}}
     */
    uiProps: function () {
        //Если не задан размер полей ввода, то берем всю оставшуюся часть после label
        var frmSize = this.props.uiInputSizes || 12 - parseInt(this.props.uiLabelSize);
        return {
            size: 'col-md-' + this.props.uiSize + ' col-sm-' + this.props.uiSize + ' col-xs-' + this.props.uiSize,
            labelSize: 'col-md-' + this.props.uiLabelSize + ' col-sm-' + this.props.uiLabelSize + ' col-xs-' + this.props.uiLabelSize,
            form: 'col-md-' + frmSize + ' col-sm-' + frmSize + ' col-xs-' + frmSize + ' form-group'
        }
    },

    render: function () {
        var uiProps = this.uiProps();

        var minPrice = this.props.minPrice ? accounting.formatNumber(this.props.minPrice, 0, " ") : "";
        var maxPrice = this.props.maxPrice ? accounting.formatNumber(this.props.maxPrice, 0, " ") : "";
        var onKeyPress = this.props.onKeyPress;

        return (
            <div className={this.props.className}>
                <div className="input-group">
                    <div className="input-group">
                        <span className="input-group-addon label-success disabled" id="sizing-addon-price-range-desc" style={{
                            color: 'white',
                            borderRadius: '0px',
                            backgroundColor: 'rgba(65, 150, 65, 1)'
                        }}>Цена</span>
                        <input type="text"
                            className="form-control"
                            aria-describedby="sizing-addon-price-range-desc"
                            placeholder='От' value={minPrice}
                            onChange={this.props.onMinPriceChange} onKeyPress={onKeyPress}
                            style={{paddingTop: 0, paddingBottom: 0}}
                        />
                        <span className="input-group-addon label-success disabled" id="sizing-addon-price-value-desc" style={{
                            color: 'white',
                            borderRadius: '0',
                            backgroundColor: 'rgba(65, 150, 65, 1)'
                        }} >:</span>
                        <input type="text"
                            className="form-control"
                            aria-describedby="sizing-addon-price-value-desc"
                            placeholder='До' value={maxPrice}
                            onChange={this.props.onMaxPriceChange} onKeyPress={onKeyPress}
                            style={{paddingTop: 0, paddingBottom: 0, borderRadius: 0}}
                        />

                    </div>
                </div>
            </div>
        )
    }
});

module.exports = PriceRange;