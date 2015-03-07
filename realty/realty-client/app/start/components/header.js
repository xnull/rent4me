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
            <div style={sectionStyle}>
                <div>
                    <AuthComponent />
                </div>
                <section id='header' className='dark'>
                    <header>
                        <h1 style={{color: '#da5037'}}>Аренда недвижимости</h1>
                        <p>Прозрачность, отсутствие посредников</p>
                    </header>
                    <footer>
                        <div className="row">

                            <div className="col-centered">
                                <input type="submit" className="button" value="Подробности" onClick={ function () {
                                    window.location.href = "#first"
                                }}/>
                            </div>
                        </div>
                    </footer>
                </section>
            </div>
        )
    }
});

module.exports = HeaderComponent;