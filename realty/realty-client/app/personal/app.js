
var React = require('react');

require('./components/router.js');
var LeftPaneComponent = require('./components/left-pane/left-pane.js');

React.render(<LeftPaneComponent/>, document.getElementById('leftPane'));

var Auth = require('../shared/common/Auth');
var Utils = require('../shared/common/Utils');

//export to the window
window.Auth = Auth;
window.Utils = Utils;
window.Cookies = require('../shared/common/Cookies');