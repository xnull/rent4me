/** @jsx React.DOM */

var LegalComponent = React.createClass({
    render: function () {
        return (
            <div className="panel panel-default">
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