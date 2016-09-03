/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'

export default class LoginModal extends Component {
    render() {

        return (
            <div className="modal fade" id="login-modal" tabIndex="-1" role="dialog" aria-labelledby="Login"
                 aria-hidden="true">
                <div className="modal-dialog login-modal-content">
                    <div className="modal-content">

                        <div className="modal-header">
                            <button type="button" className="close" data-dismiss="modal"><span
                                aria-hidden="true">&times;</span><span
                                className="screen-reader-text">Close</span></button>
                        </div>

                        <div className="modal-body">

                            <ul className="nav nav-tabs" role="tablist" style={{marginBottom: '1em'}}>
                                <li className="active"><a href="#tab-login" role="tab" data-toggle="tab">Login</a></li>
                            </ul>

                            <div className="tab-content">

                                <div className="tab-pane active" id="tab-login">

                                    <p id="msg-login-to-add-favorites" className="alert alert-info hide">
                                        <small>You have to be logged in to use this feature.</small>
                                    </p>


                                    <form name="loginform" id="loginform"
                                          method="post">

                                        <p className="login-username">
                                            <label htmlFor="user_login"></label>
                                            <input type="text" name="log" id="user_login" className="input" value=""
                                                   size="20" placeholder="Username or em@il"/>
                                        </p>
                                        <p className="login-password">
                                            <label htmlFor="user_pass"></label>
                                            <input type="password" name="pwd" id="user_pass" className="input" value=""
                                                   size="20" placeholder="Password"/>
                                        </p>

                                        <p className="login-remember"><label><input name="rememberme" type="checkbox"
                                                                                    id="rememberme"
                                                                                    value="forever"/> Remember
                                            Me</label></p>
                                        <p className="login-submit">
                                            <input type="submit" name="wp-submit" id="wp-submit-login"
                                                   className="button-primary"
                                                   value="Log In"/>
                                            <input type="hidden" name="redirect_to"
                                                   value=""/>
                                        </p>
                                        <a href="#">Lost
                                            Password?</a>
                                    </form>

                                </div>


                            </div>

                        </div>

                    </div>
                </div>
            </div>
        )
    }
}
