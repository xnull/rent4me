/**
 * Created by null on 02.01.15.
 */
var React = require('react');
var AuthActions = require('../../shared/actions/AuthActions');

var SocialNetAuth = React.createClass({
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

var AuthModalDialog = React.createClass({
    render: function () {
        return (
            <div className="modal fade" id="myModal" tabIndex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
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
                            <SocialNetAuth />
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
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

module.exports = AuthComponent;
