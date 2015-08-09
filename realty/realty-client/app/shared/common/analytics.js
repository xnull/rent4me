/**
 * Created by null on 03.01.15.
 */
var Utils = require('rent4meUtil');

function googleAnalytics() {
    console.log('Init google analytics');

    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
            (i[r].q = i[r].q || []).push(arguments)
        }, i[r].l = 1 * new Date();
        a = s.createElement(o),
            m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
    })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');

    ga('create', 'UA-52484973-1', 'auto');
    ga('require', 'displayfeatures');
    ga('send', 'pageview');

    return ga;
}

var GaHolder = (function () {
    var instance;

    function GaHolder() {
        this.ga = googleAnalytics();
    }

    return {
        getInstance: function(){
            if (instance == null) {
                instance = new GaHolder();
                // Hide the constructor so the returned objected can't be new'd...
                instance.constructor = null;
            }
            return instance;
        }
    };
})();

function yandexMetrika() {
    console.log('Init yandex metrika');

    (function (d, w, c) {
        (w[c] = w[c] || []).push(function () {
            try {
                w.yaCounter27768255 = new Ya.Metrika({
                    id: 27768255,
                    webvisor: true,
                    clickmap: true,
                    trackLinks: true,
                    accurateTrackBounce: true,
                    trackHash: true
                });
            } catch (e) {
            }
        });

        var n = d.getElementsByTagName("script")[0],
            s = d.createElement("script"),
            f = function () {
                n.parentNode.insertBefore(s, n);
            };
        s.type = "text/javascript";
        s.async = true;
        s.src = (d.location.protocol == "https:" ? "https:" : "http:") + "//mc.yandex.ru/metrika/watch.js";

        if (w.opera == "[object Opera]") {
            d.addEventListener("DOMContentLoaded", f, false);
        } else {
            f();
        }
    })(document, window, "yandex_metrika_callbacks");
}

function Segment() {
    !function () {
        var analytics = window.analytics = window.analytics || [];
        if (!analytics.initialize)if (analytics.invoked)window.console && console.error && console.error("Segment snippet included twice."); else {
            analytics.invoked = !0;
            analytics.methods = ["trackSubmit", "trackClick", "trackLink", "trackForm", "pageview", "identify", "group", "track", "ready", "alias", "page", "once", "off", "on"];
            analytics.factory = function (t) {
                return function () {
                    var e = Array.prototype.slice.call(arguments);
                    e.unshift(t);
                    analytics.push(e);
                    return analytics
                }
            };
            for (var t = 0; t < analytics.methods.length; t++) {
                var e = analytics.methods[t];
                analytics[e] = analytics.factory(e)
            }
            analytics.load = function (t) {
                var e = document.createElement("script");
                e.type = "text/javascript";
                e.async = !0;
                e.src = ("https:" === document.location.protocol ? "https://" : "http://") + "cdn.segment.com/analytics.js/v1/" + t + "/analytics.min.js";
                var n = document.getElementsByTagName("script")[0];
                n.parentNode.insertBefore(e, n)
            };
            analytics.SNIPPET_VERSION = "3.0.1";
            analytics.load("FKxFqHBDWFYItvbFuPy2lke28dweZfJb");
            analytics.page()
        }
    }();
}

function initAnalyticsSystem() {
    GaHolder.getInstance();
    yandexMetrika();
}

var Analytics = {
    initAnalyticsSystem: initAnalyticsSystem,
    google: googleAnalytics,
    yandex: yandexMetrika,
    segment: Segment,
    googleAnalyricsHolder: GaHolder.getInstance
};

var AnalyticsStub = {
    initAnalyticsSystem: function(){},
    google:  function(){},
    yandex:  function(){},
    segment:  function(){},
    googleAnalyricsHolder:  function(){
        return {
            ga : function(p1, p2, p3){}
        }
    }
};

module.exports = Utils.isLocalhost() ? AnalyticsStub : Analytics;