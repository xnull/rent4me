var React = require('react');
var PersonalCabinetComponent = require('./personal.js');
var NewsComponent = require('./news.js');
var LegalComponent = require('./legal.js');

var Auth = require('../../../shared/common/auth.js');
var Utils = require('../../../shared/common/utils.js');

var UserPanel = React.createClass({
    componentDidMount: function(root){
        $('#logoutBtn').on('click', function(){
            Auth.logoutOnBackend();
        });
    },
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

                    <div className="col-md-6">
                        <a className="btn btn-danger" href="#" id="logoutBtn">Выход</a>
                    </div>

                    <div className="col-md-6">
                        <a className="btn btn-success" href="#/user/landlord">Я собственник</a>
                    </div>

                </div>
            </div>
        );
    }
});

module.exports = React.createClass({
    render: function () {
        var develop = false;

        return (
            <div className="col-md-3">
                <UserPanel/>
                <PersonalCabinetComponent/>
                { develop ? <NewsComponent /> : null }
                { develop ? <LegalComponent/> : null }
            </div>
        )
    }
});
