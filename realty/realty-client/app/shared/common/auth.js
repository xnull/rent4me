/**
 * Created by dionis on 28/11/14.
 */
var AuthClass = function() {
    this. __base64Encode = function (data) {
        //  discuss at: http://phpjs.org/functions/base64_encode/
        // original by: Tyler Akins (http://rumkin.com)
        // improved by: Bayron Guevara
        // improved by: Thunder.m
        // improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
        // improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
        // improved by: Rafał Kukawski (http://kukawski.pl)
        // bugfixed by: Pellentesque Malesuada
        //   example 1: base64_encode('Kevin van Zonneveld');
        //   returns 1: 'S2V2aW4gdmFuIFpvbm5ldmVsZA=='
        //   example 2: base64_encode('a');
        //   returns 2: 'YQ=='
        //   example 3: base64_encode('✓ à la mode');
        //   returns 3: '4pyTIMOgIGxhIG1vZGU='

        var b64 = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
        var o1, o2, o3, h1, h2, h3, h4, bits, i = 0,
            ac = 0,
            enc = '',
            tmp_arr = [];

        if (!data) {
            return data;
        }

        data = unescape(encodeURIComponent(data));

        do {
            // pack three octets into four hexets
            o1 = data.charCodeAt(i++);
            o2 = data.charCodeAt(i++);
            o3 = data.charCodeAt(i++);

            bits = o1 << 16 | o2 << 8 | o3;

            h1 = bits >> 18 & 0x3f;
            h2 = bits >> 12 & 0x3f;
            h3 = bits >> 6 & 0x3f;
            h4 = bits & 0x3f;

            // use hexets to index into b64, and append result to encoded string
            tmp_arr[ac++] = b64.charAt(h1) + b64.charAt(h2) + b64.charAt(h3) + b64.charAt(h4);
        } while (i < data.length);

        enc = tmp_arr.join('');

        var r = data.length % 3;

        return (r ? enc.slice(0, r - 3) : enc) + '==='.slice(r || 3);
    };

    //TODO: un-hardcode it and move.
    this.userNamePasswordHardCode = this.__base64Encode("user_083af554-d3f8-4644-9090-ab50cfb612e1:eccafc45-2c2c-4028-931a-648975605899");

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
        if (response.status === 'connected') {
            // Logged into your app and Facebook.
//            testAPI();
            //redirect to personal page
            alert('INFO: Logged in via FB');
            alert('TODO: check cookies');
            alert('TODO: if has cookies - redirect to personal page');
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

    this.checkLoginState = function() {
        var that = this;
        FB.getLoginStatus(function(response) {
            that.statusChangeCallback(response);
        });
    };


};

module.exports = new AuthClass();