/**
 * Created by null on 07.01.15.
 */
var React = require('react');

var Metro = React.createClass({

    componentDidMount: function () {
        console.log('The metro component is mounted');
    },

    render: function () {
        return (
            <div className='row'>
                <div className='col-sm-12 col-md-12 col-xs-12'>
                    <div className="input-group">
                        <input type="text" className="form-control" aria-label="..." placeholder="Станция метро"/>
                        <div className="input-group-btn">
                            <button type="button" className="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Выбрать
                                <span className="caret"></span>
                            </button>
                            <ul className="dropdown-menu dropdown-menu-right" role="menu">
                                <li>
                                    <a href="#">Станция цветной бульвар</a>
                                </li>
                                <li>
                                    <a href="#">Новослободская</a>
                                </li>
                                <li>
                                    <a href="#">1905 года</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = Metro;