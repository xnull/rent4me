require('react');

require('./components/router.js');
var LeftPaneComponent = require('./components/left-pane/left-pane.js');

React.render(<LeftPaneComponent/>, document.getElementById('leftPane'));