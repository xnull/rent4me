/**
 * Created by null on 07.01.15.
 */

var React = require('react');

var PriceRange = React.createClass({

    render: function () {
        return (
            <div className='col-md-5'>
                <div className="col-md-3">
                    <label className="control-label">Цена</label>
                </div>
                <div className="col-md-9 input-group">
                    <div className="col-md-6">
                        <input type="text" className="form-control" placeholder="От"/>
                    </div>

                    <div className="col-md-6">
                        <input type="text" className="form-control" placeholder="До"/>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = PriceRange;