/**
 * Created by null on 07.01.15.
 */

var React = require('react');

var Rooms = React.createClass({
    propTypes: {
        uiSize: React.PropTypes.number.isRequired,
        uiLabelSize: React.PropTypes.number.isRequired
    },

    getDefaultProps: function () {
        return {
            oneRoomAptSelected: false,
            twoRoomAptSelected: false,
            threeRoomAptSelected: false
        };
    },

    render: function () {
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
            <div className='col-md-4'>
                <div className="col-md-3">
                    <label className="control-label">Комнат</label>
                </div>
                <div className="col-md-9">
                    <div className="btn-group" data-toggle="buttons">
                        <label className={firstClassNames} onClick={onOneRoomAptValueChanged}>
                            <input type="checkbox"
                                checked={firstSelected}
                            >1</input>
                        </label>
                        <label className={secondClassNames} onClick={onTwoRoomAptValueChanged}>
                            <input type="checkbox"
                                checked={secondSelected}
                            >2</input>
                        </label>
                        <label className={thirdClassNames} onClick={onThreeRoomAptValueChanged}>
                            <input type="checkbox"
                                checked={thirdSelected}
                            >3</input>
                        </label>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = Rooms;