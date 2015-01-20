/**
 * Меню для всего что связано с соц сетями, на момент написания это пункты:
 *  - поиска объявлений в соц сетях
 *  - выставление объявления в виде записи на своей стене вконтакте, фейсбуке и т.д.
 *  - поиск объявлений по друзьям-друзьям друзей и т.д. (возможно даже отображение социального графа через кого вы связаны с этим человеком)
 * Created by null on 07.12.14.
 */
var React = require('react');
var Util = require('rent4meUtil');

module.exports = React.createClass({
    render: function () {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <h4>Социальные сети</h4>

                    <div className="bs-component">
                        <div className="list-group">

                            <a href="#/social/fb" className="list-group-item">
                                <h4 className="list-group-item-heading">Facebook</h4>
                                <p className="list-group-item-text">Горячие объявления прямиком из социальной сети FB.</p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});