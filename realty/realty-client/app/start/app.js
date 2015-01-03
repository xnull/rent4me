/**
 * Created by dionis on 02/12/14.
 */
var React = require('react');

var AuthActions = require('../shared/actions/AuthActions');
var AuthStore = require('../shared/stores/AuthStore');
var Utils = require('../shared/common/Utils');

var FooterComponent = require('./components/footer');
var FirstDisplayComponent = require('./components/first-display');
var HeaderComponent = require('./components/header');

var Analytics = require('../shared/common/analytics');

function fbAuth() {
    $(document).ready(function () {
        AuthActions.restoreUsernameAndTokenFromCookies();
        if (!AuthStore.hasCredentials()) {
            $.ajaxSetup({cache: true});
            $.getScript('//connect.facebook.net/en_US/all.js', function () {
                FB.init({
                    appId: AuthStore.getFbId(),
                    xfbml: true,
                    cookie: true,
                    version: 'v2.1'
                });

                //TODO: don't check login state
                //AuthActions.checkLoginState();
            });


        } else {
            //TODO: redirect to personal page
            //alert("Already authorized. You could proceed");
            Utils.navigateToPersonal();
        }
    });
}

fbAuth();

React.render(<HeaderComponent/>, document.getElementById('headerComponent'));
React.render(<FirstDisplayComponent/>, document.getElementById('firstDisplay'));
React.render(<FooterComponent/>, document.getElementById('footerComponent'));

Analytics.google();
Analytics.yandex();