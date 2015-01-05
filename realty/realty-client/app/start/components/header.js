/**
 * Created by null on 03.01.15.
 */

var React = require('react');
var AuthComponent = require('./socialNetAuth');

var HeaderComponent = React.createClass({
    render: function () {
        var sectionStyle = {
            backgroundImage: "url('images/flats/kitchen1.jpg')",
            backgroundSize: 'cover',
            backgroundPosition: 'auto',
            backgroundRepeat: 'no-repeat'
        };
        return (
            <section id='header' className='dark' style={sectionStyle}>
                <header>
                    <h1 style={{color: '#da5037'}}>Аренда недвижимости</h1>
                    <p>Прозрачность, отсутствие посредников</p>
                </header>
                <footer>
                    <div className="row">
                        <div className="col-centered">
                            <AuthComponent />
                        </div>
                        <div className="col-centered">
                            <a href='#first' className='button scrolly'>Подробности</a>
                        </div>
                    </div>
                </footer>
            </section>
        )
    }
});

module.exports = HeaderComponent;