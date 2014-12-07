/**
 * Панель личного кабинета пользователя, здесь находятся все необходимые пользователю инструменты для работы. В ней отображаются:
 *  - новости интересные пользователю (лента новостей с главной страницы)
 *  - поиск по соц сетям
 *  - кнопки манипулирования объявлениями - выставить квартиру, выставить объявление о поиске жилья и т.д.
 *
 */
var React = require('react');

module.exports = React.createClass({
    render: function () {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <h4>Личный кабинет</h4>

                    <div className="bs-component">
                        <div className="list-group">
                            <a href="#/" className="list-group-item">
                                <h4 className="list-group-item-heading">Новости</h4>

                                <p className="list-group-item-text">Ваши новости</p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});