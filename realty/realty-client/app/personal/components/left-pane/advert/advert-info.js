/**
 * Advert info page
 * Created by null on 6/29/15.
 */
var React = require('react');

var AdvertManagement = require('./advert-management');
var AdvertContent = require('./advert-content');
var CommentsWidget = require('./comments-widget');

var Utils = require('rent4meUtil');

var AdvertText = React.createClass({
    getText: function(){
        return {__html: Utils.nl2br(this.props.item.description)}
    },

    render: function () {
        return (
            <div className="row">
                <div className="col-xs-12 col-md-12">
                    <table className="table table-striped">
                        <tbody>
                        <tr>
                            <td>
                                <div dangerouslySetInnerHTML={this.getText()}/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
});

var Likes = React.createClass({
    render: function () {
        return (
            <div className="row">
                <div className="col-xs-6 col-md-offset-6 text-right">
                    <div className="row">
                        <div className="col-xs-7">
                            <h4>Поделиться </h4>
                        </div>
                        <div className="col-xs-4">
                            <img
                                src="https://cdn0.iconfinder.com/data/icons/social-flat-rounded-rects/512/vkontakte-32.png"
                                width="32"/>
                            <img
                                src="https://cdn0.iconfinder.com/data/icons/social-flat-rounded-rects/512/facebook-32.png"
                                width="32"/>
                            <img
                                src="https://cdn0.iconfinder.com/data/icons/social-flat-rounded-rects/512/odnoklassniki-32.png"
                                width="32"/>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

var AdvertInfo = React.createClass({
    render: function () {
        var item = this.props.item;

        return (
            <div>
                <!--  Панель управления с инфой о владельце объявления и кнопками для жалоб и связи -->
                <AdvertManagement item={item}/>

                <div className="row">
                    <div className="col-md-12 col-sm-12 col-xs-12">
                        <AdvertContent item={item}/>
                        <AdvertText item={item}/>
                        <Likes item={item}/>
                        <!-- <CommentsWidget item={item}/> -->
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = AdvertInfo;