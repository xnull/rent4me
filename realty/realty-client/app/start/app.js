/**
 * Created by dionis on 02/12/14.
 */
var React = require('react');

var AuthActions = require('../shared/actions/AuthActions');
var AuthStore = require('../shared/stores/AuthStore');
var Utils = require('../shared/common/Utils');

var FooterComponent = require('./components/footer');
var FirstDisplayComponent = require('./components/first-display');
var SecondDisplayComponent = require('./components/second-display');
var HeaderComponent = require('./components/header');

var SkelInit = require('./components/skel-init');
var Analytics = require('../shared/common/analytics');

Analytics.initAnalyticsSystem();
SkelInit.init();



AuthActions.initFbAuth();

React.render(<HeaderComponent/>, document.getElementById('headerComponent'));
React.render(<FirstDisplayComponent/>, document.getElementById('firstDisplay'));
React.render(<SecondDisplayComponent/>, document.getElementById('secondDisplay'));
React.render(<FooterComponent/>, document.getElementById('footerComponent'));
