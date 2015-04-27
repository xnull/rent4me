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
                        <div className="col-md-offset-9 col-sm-offset-9 col-xs-offset-6 col-md-3 col-sm-3 col-xs-6">
                            <a className="btn btn-primary center-block" href='#'>Вернуться к поиску</a>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = Advert;
