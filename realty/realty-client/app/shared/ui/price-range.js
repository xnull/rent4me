/**
 * Created by null on 07.01.15.
 */

var React = require('react');
var accounting = require('accounting');

var PriceRange = React.createClass({
    propTypes: {
        uiSize: React.PropTypes.number.isRequired,
        uiLabelSize: React.PropTypes.number.isRequired,
        minPrice: React.PropTypes.number,
        maxPrice: React.PropTypes.number,
        onKeyPress: React.PropTypes.func
    },

    /**
     * Adjust UI settings
     * @returns {{size: string, labelSize: string, formSize: string}}
     */
    uiProps: function () {
        //Если не задан размер полей ввода, то берем всю оставшуюся часть после label
        var frmSize = this.props.uiInputSizes || 12 - parseInt(this.props.uiLabelSize);
        return {
            size: 'col-md-' + this.props.uiSize,
            labelSize: 'col-md-' + this.props.uiLabelSize,
            form: 'col-md-' + frmSize + ' form-group'
        }
    },

    render: function () {
        var uiProps = this.uiProps();

        var minPrice = accounting.formatNumber(this.props.minPrice, 0, " ");
        var maxPrice = accounting.formatNumber(this.props.maxPrice, 0, " ");
        var onKeyPress = this.props.onKeyPress;

        return (
            <div className={uiProps.size}>
                <div className={uiProps.labelSize}>
                    <label className='control-label pull-right'>Цена</label>
                </div>
                <div className={uiProps.form}>
                    <div className='col-md-6' style={{paddingRight: 0}}>
                        <input type='text' className='form-control' placeholder='От' value={minPrice} onChange={this.props.onMinPriceChange} onKeyPress={onKeyPress}/>
                    </div>

                    <div className='col-md-6' style={{paddingLeft: 0}}>
                        <input type='text' className='form-control' placeholder='До' value={maxPrice} onChange={this.props.onMaxPriceChange} onKeyPress={onKeyPress}/>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = PriceRange;