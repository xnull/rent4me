/**
 * Created by null on 07.01.15.
 */

var React = require('react');
var assign = require('object-assign');

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

    getInitialState: function () {
        var self = this;
        return {
            oneRoomAptSelected: self.props.oneRoomAptSelected,
            twoRoomAptSelected: self.props.twoRoomAptSelected,
            threeRoomAptSelected: self.props.threeRoomAptSelected,
            isChecked: true
        };
    },

    onOneRoomAptValueChanged: function (e) {
        var value = !this.state.oneRoomAptSelected;
        console.log('1 room apt selected? ' + value);
        this.setState(assign({}, this.state, {oneRoomAptSelected: value}));
        this.props.onOneRoomAptValueChanged(value);
    },

    onTwoRoomAptValueChanged: function (e) {
        var value = !this.state.twoRoomAptSelected;
        console.log('2 room apt selected? ' + value);
        this.setState(assign({}, this.state, {twoRoomAptSelected: value}));
        this.props.onTwoRoomAptValueChanged(value);
    },

    onThreeRoomAptValueChanged: function (e) {
        var value = !this.state.threeRoomAptSelected;
        console.log('3 room apt selected? ' + value);
        this.setState(assign({}, this.state, {threeRoomAptSelected: value}));
        this.props.onThreeRoomAptValueChanged(value);
    },

    render: function () {
        var firstSelected = this.state.oneRoomAptSelected;
        var secondSelected = this.state.twoRoomAptSelected;
        var thirdSelected = this.state.threeRoomAptSelected;

        var firstClassNames = "btn btn-default" + (firstSelected ? " active" : "");
        var secondClassNames = "btn btn-default" + (secondSelected ? " active" : "");
        var thirdClassNames = "btn btn-default" + (thirdSelected ? " active" : "");

        return (

            <div className={this.props.className}>
                <div className="btn-group btn-group-justified" data-toggle="buttons">
                    <label className="btn btn-success" style={{color: 'white', width: '2%', pointerEvents: 'none'}}>
                        Комнат
                    </label>
                    <div className={firstClassNames} onClick={this.onOneRoomAptValueChanged}>
                        1
                    </div>
                    <div className={secondClassNames} onClick={this.onTwoRoomAptValueChanged}>
                        2
                    </div>
                    <div className={thirdClassNames} onClick={this.onThreeRoomAptValueChanged}>
                        3
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = Rooms;