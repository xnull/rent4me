//var $ = require('jquery');
var Cookies = require('./cookies.js');

var JSON = require('JSON2');

var Utils = require('./utils.js');

/**
 * Created by dionis on 28/11/14.
 */
var AuthClass = function() {
    this.username = null;
    this.token = null;

    this.fbUserId = null;
    this.fbAccessToken = null;

    this.getFbId = function() {
        var isProduction = window.location.href.indexOf('rent4.me') != -1;

        var fbAppId;
        var vkAppId;
        if (isProduction) {
            fbAppId = '270007246518198';
            vkAppId = '4463597';
        } else {
            fbAppId = '271375949714661';
            vkAppId = '4463597';
        }

        return fbAppId;
    };

    this.statusChangeCallback = function (response) {
        console.log(response);
        if (response.status === 'connected') {


            this.fbUserId = response.authResponse.userID;
            this.fbAccessToken = response.authResponse.accessToken;

            this.authOnBackendWithFacebook();
        } else if (response.status === 'not_authorized') {
            // The person is logged into Facebook, but not your app.
//            document.getElementById('status').innerHTML = 'Please log ' +
//                    'into this app.';
        } else {
            // The person is not logged into Facebook, so we're not sure if
            // they are logged into this app or not.
//            document.getElementById('status').innerHTML = 'Please log ' +
//                    'into Facebook.';
        }
    };

    this.authOnBackendWithFacebook = function() {
        var that = this;
        $.blockUI();

        var data = {"facebook_id": this.fbUserId, "access_token": this.fbAccessToken};

        console.log('data to send');
        console.log(data);

        $.ajax({
            url: '/rest/auth/facebook',
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            beforeSend: function (request) {
//                request.setRequestHeader("Authorization", "Basic " + Auth.getAuthHeader());
            },
            success: function (data) {
                console.log("Success!");
                console.log("Data:");
                console.log(data);

                that.username = data.username;
                that.token = data.token;

                that.storeUsernameAndTokenInCookies();

                $.unblockUI();

                Utils.navigateToPersonal();
//                    that.setState({data: data});
            }.bind(that),
            error: function (xhr, status, err) {
//                    console.error('/rest/apartment', status, err.toString());
                console.log("Error!");
                that.username = null;
                that.token = null;
                that.fbUserId = null;
                that.fbAccessToken = null;
                $.unblockUI();
            }.bind(that)
        });
    };

    this.checkLoginState = function() {
        var that = this;
        FB.getLoginStatus(function(response) {
            that.statusChangeCallback(response);
        });
    };

    this.restoreUsernameAndTokenFromCookies = function() {
        this.username = Cookies.getCookie("rent4me_uname");
        this.token = Cookies.getCookie("rent4me_token");
    };

    this.storeUsernameAndTokenInCookies = function() {
        if(this.hasCredentials()) {
            Cookies.setCookieTemp("rent4me_uname", this.username) ;
            Cookies.setCookieTemp("rent4me_token", this.token);
        }
    };

    this.hasCredentials = function() {
        return this.username && this.token;
    };

    //TODO: un-hardcode.
    this.getAuthHeader = function(){
        if(this.hasCredentials()) {
            return Utils.__base64Encode(this.username+":"+this.token);
        } else {
            return null;
        }
    }
};

module.exports = new AuthClass();