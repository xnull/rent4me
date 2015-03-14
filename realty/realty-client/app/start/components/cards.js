var React = require('react');

var Cards = React.createClass({
    render: function () {
        var textStyle = {fontSize: 'large'};
        var footerStyle = {backgroundColor: 'rgba(0,0,0,.65)', height: '120px'};

        return (
            <div className="row">
                <div className="col-md col-md-3"/>
                <div className="2u">
                    <section>
                        <div className="thumbnail" style={{padding: 0}}>
                            <div className="panel-footer" style={footerStyle}>
                                <strong style={textStyle}>Поиск объявлений от собственников в социальных сетях</strong>
                            </div>
                        </div>
                    </section>
                </div>

                <div className="2u">
                    <section>
                        <div className="thumbnail" style={{padding: 0}}>

                            <div className="panel-footer" style={footerStyle}>
                                <strong style={textStyle}>Система препятствующая размещению объявлений риэлторами</strong>
                            </div>
                        </div>
                    </section>
                </div>

                <div className="2u">
                    <section>
                        <div className="thumbnail" style={{padding: 0}}>

                            <div className="panel-footer" style={footerStyle}>
                                <strong style={textStyle}>Абсолютно бесплатный доступ</strong>
                                <br/>
                                <br/>

                            </div>
                        </div>
                    </section>
                </div>
            </div>
        )
    }
});

module.exports = Cards;