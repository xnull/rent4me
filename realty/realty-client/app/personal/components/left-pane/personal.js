/** @jsx React.DOM */

var PersonalCabinetComponent = React.createClass({
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

/* <a href="#" className="list-group-item">
 <h4 className="list-group-item-heading">Соц. сети</h4>

 <p className="list-group-item-text">Поиск недвижимости для аренды в социальных сетях</p>
 </a>
 <a href="#" className="list-group-item">
 <h4 className="list-group-item-heading">Арендатору</h4>

 <p className="list-group-item-text"> Поиск недвижимости для аренды</p>
 </a>
 <a href="#" className="list-group-item">
 <h4 className="list-group-item-heading">Снять квартиру</h4>

 <p className="list-group-item-text">Выставить объявление о поиске жилья</p>
 </a>
 <a href="#" className="list-group-item">
 <h4 className="list-group-item-heading">Арендодателю</h4>

 <p className="list-group-item-text">Поиск арендаторов</p>
 </a>
 <a href="#" className="list-group-item">
 <h4 className="list-group-item-heading">Сдать квартиру</h4>

 <p className="list-group-item-text">Выставить объявление о сдаче квартиры в аренду</p>
 </a>
 */