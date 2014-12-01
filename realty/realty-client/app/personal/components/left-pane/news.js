/**
 * Панель новостей и аналитики, в общем всего что связано с экономической составляющей вопроса
 */
var React = require('react');

module.exports = React.createClass({
    render: function () {
        return (
            <div className="panel panel-default">

                <div className="panel-body">
                    <h4>Недвижимость</h4>

                    <div className="bs-component">
                        <div className="list-group">
                            <a href="#" className="list-group-item">
                                <p className="list-group-item-text">Новости</p>
                            </a>
                            <a href="#" className="list-group-item">
                                <p className="list-group-item-text">Аналитика</p>
                            </a>
                            <a href="#" className="list-group-item">
                                <p className="list-group-item-text">Цены на жилье по районам</p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});
