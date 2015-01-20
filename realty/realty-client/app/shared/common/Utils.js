/**
 * Created by dionis on 02/12/14.
 */
function nl2br(str) {
    var is_xhtml = true;
    //  discuss at: http://phpjs.org/functions/nl2br/
    // original by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // improved by: Philip Peterson
    // improved by: Onno Marsman
    // improved by: Atli Þór
    // improved by: Brett Zamir (http://brett-zamir.me)
    // improved by: Maximusya
    // bugfixed by: Onno Marsman
    // bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    //    input by: Brett Zamir (http://brett-zamir.me)
    //   example 1: nl2br('Kevin\nvan\nZonneveld');
    //   returns 1: 'Kevin<br />\nvan<br />\nZonneveld'
    //   example 2: nl2br("\nOne\nTwo\n\nThree\n", false);
    //   returns 2: '<br>\nOne<br>\nTwo<br>\n<br>\nThree<br>\n'
    //   example 3: nl2br("\nOne\nTwo\n\nThree\n", true);
    //   returns 3: '<br />\nOne<br />\nTwo<br />\n<br />\nThree<br />\n'

    var breakTag = (is_xhtml || typeof is_xhtml === 'undefined') ? '<br ' + '/>' : '<br>'; // Adjust comment to avoid issue on phpjs.org display

    return (str + '')
        .replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1' + breakTag + '$2');
}


function __base64Encode(data) {
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
}

function isDev() {
    return document.location.href.indexOf('dev') != -1;
}

function navigateToPersonal() {
    var isDev = document.location.href.indexOf('localhost') != -1;
    if (isDev) {
        document.location.href = '/dev/personal';
    } else {
        document.location.href = '/personal';
    }
}

function isLocalhost() {
    return window.location.href.indexOf('localhost') != -1;
}

function isProduction() {
    return window.location.href.indexOf('rent4.me') != -1 && !isDev();
}

function navigateToStart() {
    if (document.location.href.indexOf('build-js') != -1) {
        return;
    }
    if (isLocalhost()) {
        document.location.href = '/dev/';
    } else {
        document.location.href = '/';
    }
}

function getQueryParams(paramName) {
    paramName = paramName.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regexString = "[\\?&]" + paramName + "=([^&#]*)";
    var regex = new RegExp(regexString);
    console.log('window.location=' + window.location.search);
    var found = regex.exec(window.location.search);
    if (found == null) {
        //try otherwise
        var _url = window.location.href || '';
        var _idx = _url.indexOf('?');
        if (_idx >= 0) {
            _url = _url.substr(_idx);

            found = regex.exec(_url);
            if (found == null) {
                return null;
            } else {
                return decodeURIComponent(found[1].replace(/\+/g, " "));
            }
        } else {
            return null;
        }
    }
    else
        return decodeURIComponent(found[1].replace(/\+/g, " "));
}

var R4MEUtils = {
    __base64Encode: __base64Encode,
    navigateToPersonal: navigateToPersonal,
    navigateToStart: navigateToStart,
    getQueryParams: getQueryParams,
    isLocalhost: isLocalhost,
    isDev: isDev,
    isProduction: isProduction,
    nl2br: nl2br,
    inactiveUi: {opacity: 0.6, pointerEvents: 'none'}
};

module.exports = R4MEUtils;