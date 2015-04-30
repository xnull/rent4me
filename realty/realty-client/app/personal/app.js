//export to the window
var Utils = require('../shared/common/Utils');
window.jQuery = require('jquery');
window.$ = window.jQuery;
window.bootstrap = require('bootstrap');
window.Utils = Utils;
window.Cookies = require('../shared/common/Cookies');

var React = require('react');

require('./components/router.js');

var Analytics = require('../shared/common/analytics');
var HeaderComponent = require('./components/header/Header.React');
var LeftPaneComponent = require('./components/left-pane/left-pane.js');

Analytics.initAnalyticsSystem();

React.render(<HeaderComponent/>, document.getElementById('header'));
React.render(<LeftPaneComponent/>, document.getElementById('leftPane'));
