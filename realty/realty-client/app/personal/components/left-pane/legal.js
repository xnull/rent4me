/**
 * Все что связано с юридической стороной вопроса: договор аренды, юр. вопросы, налог для собственника и т.д.
 */
var React = require('react');
var Utils = require('rent4meUtil');

module.exports = React.createClass({
    render: function () {
        return (
            <div className="panel panel-default" style={Utils.inactiveUi}>
                <div className="panel-body">
                    <h4>Квартирный вопрос</h4>

                    <div className="bs-component">
                        <div className="list-group">
                            <a href="#" className="list-group-item">
                                <h4 className="list-group-item-heading">Типовой договор</h4>
                                <p className="list-group-item-text">Типовой договор аренды недвижимости</p>
                            </a>
                            <a href="#" className="list-group-item">
                                <h4 className="list-group-item-heading">Рассылки</h4>

                                <p className="list-group-item-text">Подпишитесь на рассылки чтобы немедленно узнавать
                                    о новых объявлениях
                                </p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});
