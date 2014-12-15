
var React = require('react');

require('./components/router.js');
var HeaderComponent = require('./components/header/Header.React');
var LeftPaneComponent = require('./components/left-pane/left-pane.js');

React.render(<HeaderComponent/>, document.getElementById('header'));
React.render(<LeftPaneComponent/>, document.getElementById('leftPane'));

var Utils = require('../shared/common/Utils');

//export to the window
window.Utils = Utils;
window.Cookies = require('../shared/common/Cookies');