/**
 * Created by null on 07.01.15.
 */

var React = require('react');

var PriceRange = React.createClass({

    render: function () {
        return (
            <div className='col-md-6'>
                <div className="col-md-2">
                    <label className="control-label">Цена</label>
                </div>
                <div className="col-md-10">
                    <div className="col-md-6">
                        <input type="text" className="form-control col-md-3" placeholder="От"/>
                    </div>

                    <div className="col-md-6">
                        <input type="text" className="form-control col-md-3" placeholder="До"/>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = PriceRange;