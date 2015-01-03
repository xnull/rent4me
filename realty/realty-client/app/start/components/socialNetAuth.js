/**
 * Created by null on 02.01.15.
 */
var React = require('react');
var AuthActions = require('../../shared/actions/AuthActions');

var AuthComponent = React.createClass({

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

module.exports = AuthComponent;

/*
 <ul className="actions">
 <li>
 <input type="submit" className="button" value="Send Message"/>
 </li>
 <li>
 <input type="reset" className="button alt" value="Clear Form"/>
 </li>
 </ul>*/
/* <div>
 <form className="aui login-form"
 action="/account/signin/" method="post" id="login-form" data-modules="js/login/login-form">


 <input type="hidden" name="next" value="/account/team_check/?next=" />


 <div className="field-group username">
 <label for="id_username">Username or email</label>
 <input type="text" className="text long-field" id="id_username" name="username" autofocus="autofocus" />
 </div>


 <div className="field-group">
 <label for="id_password">Password</label>
 <input type="password" id="id_password" className="password long-field" name="password" />
 </div>

 <input type='hidden' name='csrfmiddlewaretoken' value='jcug2Km35RdQXym1hx9VLDSOFQoDqIdz' />
 <div className="buttons-container">
 <div className="buttons">
 <button type="submit" name="submit" className="aui-button aui-button-primary">
 Log in
 </button>
 <a href="/account/password/reset/" className="forgot-password">Forgot your password</a>
 </div>

 <div className="sign-up">
 <a href="/account/signup/">Need an account! Sign up free.</a>
 </div>

 </div>
 </form>
 </div>*/