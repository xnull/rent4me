/**
 * Created by null on 07.01.15.
 */

var React = require('react');

var Rooms = React.createClass({
    propTypes: {
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

            <div className={this.props.className}>
                <div className="btn-group btn-group-justified" data-toggle="buttons">
                    <label className="btn btn-success" style={{color: 'white', width: '2%', pointerEvents: 'none'}}>
                        Комнат
                    </label>
                    <label className={firstClassNames}>
                        <input type="checkbox" checked={firstSelected} onChange={onOneRoomAptValueChanged}>1</input>

                    </label>
                    <label className={secondClassNames}>
                        <input type="checkbox" checked={secondSelected} onChange={onTwoRoomAptValueChanged}>2</input>
                    </label>
                    <label className={thirdClassNames}>
                        <input type="checkbox" checked={thirdSelected} onChange={onThreeRoomAptValueChanged}>3</input>
                    </label>
                </div>
            </div>
        )
    }
});

module.exports = Rooms;