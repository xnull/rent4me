var React = require('react');

var MailChimpForm = React.createClass({
    render: function () {
        return (
            <div className="content style4 featured">
                <div className="container small">
                    <form
                        action="//rent4.us8.list-manage.com/subscribe/post?u=0a209f5f0573bb38236e00eb5&amp;id=df5a96106d"
                        method="post" name="mc-embedded-subscribe-form"
                        target="_blank">
                        <div style={{position: 'absolute', left: -5000}}>
                            <input type="text" name="b_0a209f5f0573bb38236e00eb5_df5a96106d" tabIndex="-1"
                                   value=""/>
                        </div>

                        <div className="row half">
                            <div className="12u">
                                <input type="email" name="EMAIL" placeholder="E-mail"/>
                            </div>
                        </div>

                        <div className="row">
                            <div className="12u">
                                <ul className="actions">
                                    <li>
                                        <input type="submit" className="button" value="Следить за новостями"
                                               name="subscribe" id="mc-embedded-subscribe"/>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
});

var MailChimpComponent = React.createClass({
    render: function () {
        return (
            <div className="content dark style2">
                <div className="container">
                    <div className="row">
                        <div className="12u">
                            <footer>
                                <MailChimpForm />
                            </footer>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

var SecondDisplay = React.createClass({
    render: function () {
        return (
            <section id="second" className="main">
                <header>
                    <div className="container">
                        <h2>Планы</h2>

                        <p>Также мы работаем над множеством удобных и необходимых пользователю возможностей, например:
                            <br/>Пожаловаться на объявление от агента.
                            <br/>Получение уведомлений на почту о новых объявлениях на сайте согласно фильтра.
                            <br/>Улучшение системы защиты от агентов.
                            <br/>Полная статистика для собственников и арендаторов о районе в котором находится
                            жильё, включая информацию об отношении стоимости данного жилья к средней стоимости аренды в
                            этом районе.
                            <br/>И множество других инструментов.
                        </p>
                    </div>
                </header>

                <MailChimpComponent />
            </section>
        )
    }
});

module.exports = SecondDisplay;