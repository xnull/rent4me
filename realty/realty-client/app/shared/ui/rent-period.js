/**
 * Created by null on 25.01.15.
 */
var React = require('react');

var RentPeriod = React.createClass({
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
                <div className="control-group">
                    <div className={uiProps.labelSize}>
                        <label className="control-label pull-right"></label>
                    </div>

                    <div className={uiProps.form}>
                        <div className="dropdown">
                            <button className="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
                                Срок аренды
                                <span className="caret"></span>
                            </button>
                            <ul className="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                                <li role="presentation">
                                    <a role="menuitem" tabindex="-1" href="#">Посуточно</a>
                                </li>
                                <li role="presentation">
                                    <a role="menuitem" tabindex="-1" href="#">Долгосрочная</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = RentPeriod;