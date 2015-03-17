/**
 * Created by null on 07.03.15.
 */

var React = require('react');
var SocialNetStore = require('../stores/SocialNetStore');

var RentType = React.createClass({

    propTypes: {
        changeToRenter: React.PropTypes.func.isRequired,
        changeToLessor: React.PropTypes.func.isRequired,
        className: React.PropTypes.string
    },

    render: function () {

        var isRenter = SocialNetStore.getSearchType() === 'RENTER';

        var renterClasses = "btn btn-default" + (isRenter ? " active" : "");
        var lessorClasses = "btn btn-default" + (!isRenter ? " active" : "");

        var className = this.props.className || 'col-sm-3 col-md-3 col-xs-3';

        return (
            <div className={className}>
                <div className="btn-group btn-group-justified" data-toggle="buttons" >
                    <label className="btn btn-success disabled" style={{color: 'white'}}>
                        Я хочу
                    </label>
                    <label className={renterClasses} onClick={this.props.changeToRenter} >
                        <input type="radio" >Снять</input>
                    </label>
                    <label className={lessorClasses} onClick={this.props.changeToLessor} >
                        <input type="radio" >Сдать</input>
                    </label>
                </div>
            </div>
        )
    }
});

module.exports = RentType;