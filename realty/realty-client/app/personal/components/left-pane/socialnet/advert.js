/**
 * Created by dionis on 4/18/15.
 */
var React = require('react');
var Router = require('react-router');

var Posts = require('./posts');

var SocialNetStore = require('../../../../shared/stores/SocialNetStore');
var SocialNetActions = require('../../../../shared/actions/SocialNetActions');

var NOT_INITIALIZED_YET = 'NOT_LOADED_YET';

var Advert = React.createClass({
    mixins: [Router.State],

    getInitialState: function () {
        var that = this;
        return {
            post: NOT_INITIALIZED_YET
        }
    },

    componentDidMount: function () {
        SocialNetStore.addChangeListener(this._onPostChange);
        SocialNetActions.findPost(this.getParams().id);
    },

    componentWillUnmount: function () {
        SocialNetStore.removeChangeListener(this._onPostChange);
    },

    _onPostChange: function () {
        var that = this;
        this.setState({post: SocialNetStore.getPostById(that.getParams().id)});
    },

    _smartReturnToSearch: function() {
        //вернуться к поиску с запоминанием позиции в ней
        var referrer = document.referrer || '';
        console.log('referrer: '+referrer);
        var historyLength = window.history.length;
        if((referrer.indexOf('://rent4.me') != -1 || referrer.indexOf('://localhost') != -1 || !referrer) && historyLength > 1) {
            console.log('returning back via history');
            console.log('History length: ' + historyLength);
            window.history.back();
        } else {
            console.log('returning back via link');
            document.location.href = '#/';
        }
    },

    render: function () {
        var params = this.getParams();
        console.log('Params are');
        console.log(params);
        console.log('Advert id:');
        console.log(params.id);
        var id = params.id;

        var post = this.state.post;
        var content = "Объявление не найдено";

        if (post == NOT_INITIALIZED_YET) {
            content = "Данные загружаются";
        } else if (post && post != NOT_INITIALIZED_YET) {
            content = <Posts items={[post]} shown={true} hasMore={false}
                             showFull={true}
                             onHasMoreClicked={void(0)}/>;
        }

        return (
            <div>
                <div className="panel">
                    <div className="panel-body" style={{backgroundColor: 'rgba(9, 45, 76, 0.2)'}}>
                        {content}
                    </div>
                    <div className="panel-footer">
                        <btn className="btn btn-primary center-block" onClick={this._smartReturnToSearch}>Вернуться к поиску</btn>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = Advert;
