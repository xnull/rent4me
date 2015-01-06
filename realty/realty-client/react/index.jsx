var SocialNetAuth = React.createClass({
    render: function () {
        return (
            <div className="row row-centered">
                <div className="col-md">
                    <p style={{color: 'black'}}>
                        <span>Войти с помощью</span>
                    </p>
                </div>

                <div className="col-centered">
                    <img className="clickable" width="64" src="images/signin/fb-icon.png" />
                    <br/>
                </div>

                <div className="col-centered">
                    <img className="clickable" width="65" src="images/signin/vk.png"/>
                </div>
            </div>
        )
    }
});

var PlainLogin = React.createClass({

    render: function () {
        return (
            <form role="form">
                <div className="row">
                    <div className="col-md-6">
                        <input type="email" className="form-control" id="inputEmail3" placeholder="E-mail"/>
                    </div>
                </div>
                <br/>
                <div className="row">
                    <div className="col-md-6">
                        <input type="password" className="form-control" id="inputPassword3" placeholder="Пароль"/>
                    </div>
                </div>
                <br/>
                <div className="row">
                    <div className="col-md-6">
                        <button type="submit" className="btn btn-success pull-right">Вход</button>
                    </div>
                </div>
            </form>
        )
    }
});

var AuthModalDialog = React.createClass({
    render: function () {
        return (
            <div className="modal fade" id="myModal" tabIndex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <button type="button" className="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                                <span className="sr-only">Закрыть</span>
                            </button>
                            <h3 className="modal-title" id="myModalLabel" style={{color: 'black'}}>
                                Авторизация
                            </h3>
                        </div>
                        <div className="modal-body">
                            <hr/>
                            <PlainLogin />
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-default" data-dismiss="modal">Закрыть</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

var AuthForm = React.createClass({
    render: function () {
        return (
            <div>
                <input type="submit" className="button special" value="Вход" data-toggle="modal" data-target="#myModal"/>
                <AuthModalDialog />
            </div>
        )
    }
});


var AuthComponent = React.createClass({
    render: function () {
        return <AuthForm />
    }
});

React.render(
    <AuthComponent/>
    , document.body);
