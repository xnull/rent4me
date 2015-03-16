/**
 * Created by null on 07.01.15.
 */

var React = require('react');

var Rooms = React.createClass({
    propTypes: {
        uiSize: React.PropTypes.number.isRequired,
        uiLabelSize: React.PropTypes.number.isRequired,

        onOneRoomAptValueChanged: React.PropTypes.func.isRequired,
        onTwoRoomAptValueChanged: React.PropTypes.func.isRequired,
        onThreeRoomAptValueChanged: React.PropTypes.func.isRequired,

        oneRoomAptSelected: React.PropTypes.bool,
        twoRoomAptSelected: React.PropTypes.bool,
        threeRoomAptSelected: React.PropTypes.bool
    },

    getDefaultProps: function () {
        return {
            oneRoomAptSelected: false,
            twoRoomAptSelected: false,
            threeRoomAptSelected: false
        };
    },

    uiProps: function () {
        var frmSize = 12 - parseInt(this.props.uiLabelSize);
        return {
            size: 'col-md-' + this.props.uiSize + ' col-sm-' + this.props.uiSize + ' col-lg-' + this.props.uiSize,
            labelSize: 'col-md-' + this.props.uiLabelSize + ' col-sm-' + this.props.uiLabelSize + ' col-lg-' + this.props.uiLabelSize,
            form: 'col-md-' + frmSize + ' col-sm-' + frmSize + ' col-lg-' + frmSize
        }
    },

    render: function () {
        var uiProps = this.uiProps();

        var firstSelected = this.props.oneRoomAptSelected;
        var secondSelected = this.props.twoRoomAptSelected;
        var thirdSelected = this.props.threeRoomAptSelected;

        var that = this;

        var onOneRoomAptValueChanged = function (e) {
            var value = !firstSelected;
            console.log('1 room apt selected? ' + value);
            that.props.onOneRoomAptValueChanged(value);
        };

        var onTwoRoomAptValueChanged = function (e) {
            var value = !secondSelected;
            console.log('2 room apt selected? ' + value);
            that.props.onTwoRoomAptValueChanged(value);
        };

        var onThreeRoomAptValueChanged = function (e) {
            var value = !thirdSelected;
            console.log('3 room apt selected? ' + value);
            that.props.onThreeRoomAptValueChanged(value);
        };

        var firstClassNames = "btn btn-default" + (firstSelected ? " active" : "");
        var secondClassNames = "btn btn-default" + (secondSelected ? " active" : "");
        var thirdClassNames = "btn btn-default" + (thirdSelected ? " active" : "");

        return (
            <div className={uiProps.size}>
                <div className={uiProps.labelSize}>
                    <label className="control-label pull-right">Комнат</label>
                </div>
                <div className={uiProps.form}>
                    <div className="btn-group btn-group-justified" data-toggle="buttons">
                        <label className={firstClassNames} onClick={onOneRoomAptValueChanged}>
                            <input type="checkbox"
                                checked={firstSelected}
                            >1</input>
                        </label>
                        <label className={secondClassNames} onClick={onTwoRoomAptValueChanged}>
                            <input type="checkbox" checked={secondSelected}>2</input>
                        </label>
                        <label className={thirdClassNames} onClick={onThreeRoomAptValueChanged}>
                            <input type="checkbox" checked={thirdSelected}>3</input>
                        </label>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = Rooms;