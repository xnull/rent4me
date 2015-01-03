/**
 * Created by null on 03.01.15.
 */

var React = require('react');

var MailChimpForm = React.createClass({
    render: function () {
        return (
            <div className="content style4 featured">
                <div className="container small">
                    <form action="//rent4.us8.list-manage.com/subscribe/post?u=0a209f5f0573bb38236e00eb5&amp;id=df5a96106d"
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
            <div className="content dark style1 featured">
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

var FirstComponent = React.createClass({
    render: function () {
        return (
            <section id="first" className="main">
                <header>
                    <div className="container">
                        <h2>Аренда недвижимости без посредников</h2>

                        <p className="text-justify">Возможно ли такое в наше время в России&#63; Этим вопросом задаются все,
                            кто хоть раз пытался снять квартиру.
                            <br/>
                            Реалии нашей жизни таковы, что агенства недвижимости захватили рынок аренды жилья. Найти квартиру
                            самостоятельно очень сложно.
                            <br/>
                            <strong>Итог</strong>
                            : недопустимо низкое качество предоставляемых услуг,
                            от которого стардают все кроме самих посредников, которые и являются причиной царящего хаоса на рынке.
                        </p>
                    </div>
                </header>

                <MailChimpComponent />
            </section>
        )
    }
});

module.exports = FirstComponent;