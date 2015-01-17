/**
 * Created by null on 07.01.15.
 */

var React = require('react');

var Rooms = React.createClass({
    render: function () {
        return (
            <div className='col-md-4'>
                <div className="col-md-3">
                    <label className="control-label">Комнат</label>
                </div>
                <div className="col-md-9">
                    <div className="btn-group" data-toggle="buttons">
                        <label className="btn btn-default">
                            <input type="checkbox">1</input>
                        </label>
                        <label className="btn btn-default">
                            <input type="checkbox">2</input>
                        </label>
                        <label className="btn btn-default">
                            <input type="checkbox">3</input>
                        </label>
                        <label className="btn btn-default">
                            <input type="checkbox">4+</input>
                        </label>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = Rooms;