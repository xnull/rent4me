function R4MECookies() {
    this.setCookie = function(cname, cvalue, exdays) {
        if(exdays > 0) {
            var d = new Date();
            d.setTime(d.getTime() + (exdays*24*60*60*1000));
            var expires = "expires="+d.toUTCString();
            document.cookie = cname + "=" + cvalue + "; " + expires+"; path=/";
        } else {
            document.cookie = cname + "=" + cvalue+"; path=/";
        }

    };

    this.setCookieTemp = function(cname, cvalue) {
        this.setCookie(cname, cvalue, 0);
    };

    this.setCookieForYear = function(cname, cvalue) {
        this.setCookie(cname, cvalue, 365);
    };

    this.getCookie = function(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i=0; i<ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1);
            if (c.indexOf(name) != -1) return c.substring(name.length,c.length);
        }
        return null;
    };

    this.deleteCookie = function (c_name) {
        this.setCookie(c_name,"",-1);
    };
}

module.exports = new R4MECookies();