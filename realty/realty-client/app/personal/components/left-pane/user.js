/** @jsx React.DOM */

var UserComponent = React.createClass({
    render: function () {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <h4>Пользователь</h4>

                    <div className="well well-sm">
                        <div className="media">
                            <a className="thumbnail pull-left" href="#">
                                <img className="media-object" src="//placehold.it/80"/>
                            </a>

                            <div className="media-body">
                                <h4 className="media-heading">John Doe</h4>

                                <br/>

                                <div className="col-md-12">
                                    <a className="btn btn-default center-block" href="#/user">Настройки</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-6 col-md-offset-6">
                        <a className="btn btn-success" href="#/user/landlord">Я собственник</a>
                    </div>

                </div>
            </div>
        );
    }
});

var LandlordSettings = React.createClass({
    render: function () {

        var addressProp = {name: 'Адрес'};

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Собственность</h4>

                        <br/>

                        <p>Хата в центре мира</p>

                    </div>
                </div>
            </div>
        )
    }
});

var Settings = React.createClass({
    render: function () {
        var nameProp = {name: 'Имя'};
        var phoneProp = {name: 'Телефон'};
        var emailProp = {name: 'Email'};
        var roleProp = {name: 'Роль в системе'};

        var submitButton = {url: '#/user/submit', value: 'Сохранить'};

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>НАСТРОЙКИ</h4>

                        <br/>

                        <form className="form-horizontal" role="form">
                            <UserProperty data={nameProp}/>
                            <UserProperty data={phoneProp}/>
                            <UserProperty data={emailProp}/>
                            <UserProperty data={roleProp}/>

                            <UserButton data={submitButton}/>
                        </form>

                    </div>
                </div>
            </div>
        );
    }
});

var UserProperty = React.createClass({
    render: function () {
        return (
            <div>
                <div className="form-group">
                    <label className="col-md-2 control-label">{this.props.data.name}:</label>
                    <div className="col-md-4">
                        <input
                            className="form-control"
                            type="text"
                            placeholder=""
                        />
                    </div>
                </div>
            </div>
        )
    }
});

var UserButton = React.createClass({
    render: function () {
        return (
            <div className="col-md-2 col-md-offset-4">
                <a className="btn btn-primary center-block" href={this.props.data.url}>{this.props.data.value}</a>
            </div>
        )
    }
});