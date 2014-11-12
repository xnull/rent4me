/** @jsx React.DOM */

var LentaComponent = React.createClass({
    render: function () {
        return (
            <div className="panel panel-default">
                <div className="panel-heading">
                    <h4>Нововсти</h4>
                </div>

                <div className="panel-body">

                    <div className="bs-component">
                        <div className="list-group">
                            <a href="#" className="list-group-item">
                                <h4 className="list-group-item-heading">Предложение месяца (пример)</h4>

                                <div className="panel-thumbnail">
                                    <img className="img-responsive"
                                        src="http://1.bp.blogspot.com/-8RhvioXdNaU/TpbO6jD4NwI/AAAAAAAAAMo/zw_cHZPv66s/s1600/2832815136_88231c1067_o-1-.jpg"/>
                                </div>

                                <hr/>
                                <p>Mega text yuo</p>
                                <hr/>

                                <p>
                                    <img src="http://api.randomuser.me/portraits/med/women/4.jpg" height="32px"/>
                                    <img src="http://api.randomuser.me/portraits/med/men/4.jpg" width="32px"
                                        height="28px"/>
                                </p>
                            </a>

                            <br/>

                            <a href="#" className="list-group-item">
                                <h4 className="list-group-item-heading">Второе событие</h4>

                                <p className="list-group-item-text">Ничто не вечно под луной</p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});
React.render(
    <LentaComponent />,
    document.getElementById('lentaComponent')
);