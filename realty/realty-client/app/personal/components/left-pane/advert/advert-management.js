var React = require('react');

var IfPresent = React.createClass({
    render: function () {
        return (
            <div/>
        )
    }
});

var AdvertManagement = React.createClass({

    connect: function () {
        return (
            <a href={this.props.item.external_link} className="btn btn-danger center-block"
               style={{padding: '12px'}} target="_blank">
                Связаться
            </a>
        )
    },

    render: function () {
        var item = this.props.item;

        return (
            <div className="row no-gutter">
                <div className="col-md-12">
                    <div className="panel panel-default noBorderRadius"
                         style={{marginBottom: '0 !important', borderBottom: 0}}>
                        <div className="panel-body">
                            <div className="row">
                                <!-- Фоточка разместившего объяву товарища (зареганного или фотка профиля из соц сети) -->
                                <div className="col-xs-2 col-md-1">
                                    <a href="#">
                                        <!-- super man: http://mediad.publicbroadcasting.net/p/kpcw/files/201404/superman_no_photo.png -->
                                        <img src="https://nslnr.su/pict/no_photo_man.gif" alt="..."
                                             className="img-rounded" width="38"/>
                                    </a>
                                </div>

                                <!-- Конпки связи -->
                                <div className="col-xs-3 col-md-3 ">
                                    {Utils.IfDefined(item.external_link, this.connect)}
                                </div>

                                <!-- Редактировать -->
                                <div className="col-xs-3 col-md-2 col-md-offset-3 text-center">
                                    <a className="btn btn-default">Исправить &nbsp;<img
                                        src="https://cdn0.iconfinder.com/data/icons/ikooni-outline-free-basic/128/free-18-24.png"
                                        className="glyphicon glyphicon-picture"
                                        title="Помочь в распознавании объявленияя"
                                        width="32"/>
                                    </a>
                                </div>

                                <!-- Пожаловаться -->
                                <div className="col-xs-3 col-md-2 text-center">
                                    <a className="btn btn-default">Пожаловаться&nbsp;<img
                                        src="https://cdn0.iconfinder.com/data/icons/ikooni-outline-free-basic/128/free-18-24.png"
                                        className="glyphicon glyphicon-picture"
                                        title="Пожаловаться на объявление от агента"
                                        width="32"/>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = AdvertManagement;