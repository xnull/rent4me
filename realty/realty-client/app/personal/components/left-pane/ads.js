/**
 * Все что связано с юридической стороной вопроса: договор аренды, юр. вопросы, налог для собственника и т.д.
 */
var React = require('react');
var Utils = require('rent4meUtil');

module.exports = React.createClass({
    render: function () {
        return (
            <div className='hidden-xs hidden-sm'>
                <div className="panel panel-default">
                    <div className="panel-body">
                        <div className="bs-component">
                            <div className="list-group">
                                <a href="https://play.google.com/store/apps/details?id=rent4.me.rent">
                                    <img alt="Get it on Google Play"
                                        src="https://developer.android.com/images/brand/ru_generic_rgb_wo_60.png" />
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});
