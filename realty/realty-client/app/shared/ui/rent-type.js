/**
 * Created by null on 07.03.15.
 */

var React = require('react');
var SocialNetStore = require('../stores/SocialNetStore');

var RentType = React.createClass({

    render: function () {
        console.log('maza');
        var isRenter = SocialNetStore.getSearchType() === 'RENTER';

        var renterClasses = "btn btn-default" + (isRenter ? " active" : "");
        var lessorClasses = "btn btn-default" + (!isRenter ? " active" : "");

        return (
            <div className='col-md-3'>
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