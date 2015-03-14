/**
 * Created by null on 07.03.15.
 */

var React = require('react');
var SocialNetStore = require('../stores/SocialNetStore');

var RentType = React.createClass({

    propTypes: {
        changeToRenter: React.PropTypes.func.isRequired,
        changeToLessor: React.PropTypes.func.isRequired
    },

    render: function () {

        var isRenter = SocialNetStore.getSearchType() === 'RENTER';

        var renterClasses = "btn btn-default" + (isRenter ? " active" : "");
        var lessorClasses = "btn btn-default" + (!isRenter ? " active" : "");

        var size = this.props.size || 'col-md-3';

        return (
            <div className={size}>
                <div className="btn-group" data-toggle="buttons" >
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