var React = require('react');

require('./components/router.js');
var Utils = require('../shared/common/Utils');

var Analytics = require('../shared/common/analytics');
var HeaderComponent = require('./components/header/Header.React');
var LeftPaneComponent = require('./components/left-pane/left-pane.js');

Analytics.initAnalyticsSystem();

React.render(<HeaderComponent/>, document.getElementById('header'));
React.render(<LeftPaneComponent/>, document.getElementById('leftPane'));

//export to the window
window.jQuery = require('jquery');
window.$ = window.jQuery;
window.bootstrap = require('bootstrap');
window.Utils = Utils;
window.Cookies = require('../shared/common/Cookies');
