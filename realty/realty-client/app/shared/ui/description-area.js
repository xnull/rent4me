/**
 * Created by null on 25.01.15.
 */
var React = require('react');

var Description = React.createClass({
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
            size: 'col-md-' + this.props.uiSize + ' col-sm-' + this.props.uiSize + 'col-lg-' + this.props.uiSize,
            labelSize: 'col-md-' + this.props.uiLabelSize + ' col-sm-' + this.props.uiLabelSize + ' col-lg-' + this.props.uiLabelSize,
            form: 'col-md-' + frmSize + ' col-sm-' + frmSize + ' col-lg-' + frmSize
        }
    },

    render: function () {
        var uiProps = this.uiProps();
        return (
            <div className={uiProps.size}>
                <div className="control-group">
                    <div className={uiProps.labelSize}>
                        <label className="control-label pull-right">Описание</label>
                    </div>

                    <div className={uiProps.form}>
                        <textarea className="form-control" rows="7"></textarea>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = Description;