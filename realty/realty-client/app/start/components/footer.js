/**
 * Created by null on 03.01.15.
 */

var React = require('react');

var FooterComponent = React.createClass({
    render: function () {
        console.log('Footer render');
        return (
            <section id="footer">
                <ul className="icons">
                    <li>
                        <a href="https://vk.com/rentality" className="icon fa-vk">
                            <span className="label">Twitter</span>
                        </a>
                    </li>
                    <li>
                        <a href="https://www.facebook.com/groups/1562360793999914" className="icon fa-facebook">
                            <span className="label">Facebook</span>
                        </a>
                    </li>
                </ul>
                <div className="copyright">
                    <ul className="menu">
                        <li>&copy; Designed by null. All rights reserved.</li>
                    </ul>
                </div>
            </section>
        )
    }
});

module.exports = FooterComponent;