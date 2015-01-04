/**
 * Created by null on 02.01.15.
 */
var React = require('react');
var AuthActions = require('../../shared/actions/AuthActions');

var AuthFormOld = React.createClass({
    handleFacebookLogin: function () {
        console.log('handle fb login');
        AuthActions.loginWithFB();
    },

    handleVkLogin: function () {
        console.log('handle vk login');
        AuthActions.loginWithVK();
    },

    render: function () {
        return (
            <div className="row row-centered">
                <div className="col-centered" onClick={this.handleFacebookLogin}>
                    <img className="clickable" width="100" src="images/signin/fb-icon.png" />
                    <br/>
                </div>

                <div className="col-centered" onClick={this.handleVkLogin}>
                    <img className="clickable" width="103" src="images/signin/vk.png"/>
                </div>
            </div>
        )
    }
});

var AuthForm = React.createClass({

    render: function () {
        return (
            <div className="col-centered">
                /*<a className='button' data-toggle="modal" data-target="#myModal">Авторизация</a>*/
                <div className="modal fade" id="myModal" tabindex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header">
                                <button type="button" className="close" data-dismiss="modal">
                                    <span aria-hidden="true">&times;</span>
                                    <span className="sr-only">Close</span>
                                </button>
                                <h4 className="modal-title" id="myModalLabel">Modal title</h4>
                            </div>
                            <div className="modal-body">
                                ...
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
                                <button type="button" className="btn btn-primary">Save changes</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});


var AuthComponent = React.createClass({

    render: function () {
        return (
            <div>
                <AuthFormOld />
            </div>
        )
    }
});

module.exports = AuthComponent;
