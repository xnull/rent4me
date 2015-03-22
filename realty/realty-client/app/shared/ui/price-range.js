/**
 * Created by null on 07.01.15.
 */

var React = require('react');
var accounting = require('accounting');

var PriceRange = React.createClass({
    propTypes: {
        className: React.PropTypes.string.isRequired,
        minPrice: React.PropTypes.number,
        maxPrice: React.PropTypes.number,
        onKeyPress: React.PropTypes.func,
        onMaxPriceChange: React.PropTypes.func.isRequired,
        onMinPriceChange: React.PropTypes.func.isRequired
    },

    render: function () {
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