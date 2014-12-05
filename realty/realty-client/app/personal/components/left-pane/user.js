/**
 * Компонент настроек профиля пользователя. Пользователь задает свою контактную информацию на этой панели
 */
var React = require('react');

var UserStore = require('../../../shared/stores/UserStore');
var UserActions = require('../../../shared/actions/UserActions');
var JSON = require('JSON2');

var assign = require('object-assign');

var UserProperty = React.createClass({
    render: function () {
        var customClassName = this.props.data.customClassName || 'col-md-4';

        return (
            <div>
                <div className="form-group">
                    <label className="col-md-2 control-label">{this.props.data.name}:</label>
                    <div className={customClassName}>
                        <input
                            id={this.props.data.id}
                            className="form-control"
                            type="text"
                            name={this.props.data.elementName}
                            onChange={this.props.onChange}
                            placeholder={this.props.data.placeholder}
                            value={this.props.data.elementValue}
                        />
                    </div>
                </div>
            </div>
        )
    }
});

var UserButton = React.createClass({
    render: function () {
        var customClassName = this.props.data.customClassName || 'col-md-2 col-md-offset-4';

        return (
            <div className={customClassName}>
                <a id={this.props.data.id} className="btn btn-primary center-block" href={this.props.data.url} onClick={this.props.onClick}>{this.props.data.value}</a>
            </div>
        )
    }
});

var getMyProfile = function() {
    return UserStore.getMyProfile();
};

module.exports = React.createClass({
    getInitialState: function() {
//        console.log('get initial state');
        return UserStore.getMyProfile();
    },
    componentWillMount: function() {
//        console.log('component will mount');
        UserStore.addChangeListener(this._onLoad);

        UserActions.loadMyProfile();

//        $('#saveMyInfoBtn').on('click', function(){
//            UserStore.saveMyProfile();
//        });
    },

    componentWillUnmount: function() {
        UserStore.removeChangeListener(this._onLoad);
    },

    _onSave: function(event) {
//        console.log('on save');
//        console.log(event);
//        console.log(event.target);
//
        UserActions.save(assign(getMyProfile(), this.state));
    },

    _onLoad: function() {
//        console.log('on load triggered');
        var user = UserStore.getMyProfile();
//        console.log('user:'+JSON.stringify(user));
        var newState = assign(this.state, user);

//        console.log(newState);
        this.setState(newState);
    },

    _onChange: function(event) {
        if(!event) {
//            console.log('no event specified');
            return;
        }

//        console.log('on change');
//        console.log(event);
//        console.log(event.target);
//        console.log(event.target.name);
//        console.log(event.target.value);

        var diffObj = {};
        diffObj[event.target.name] = event.target.value;

        var newState = assign(this.state, diffObj);

//        console.log(newState);
        this.setState(newState);
    },

    render: function () {
        var nameProp = {name: 'Имя', elementName: 'name', elementValue: this.state.name};
        var phoneProp = {name: 'Телефон', elementName: 'phone', elementValue: this.state.phone};
        var emailProp = {name: 'Email', elementName: 'email', elementValue: this.state.email};

        var submitButton = {value: 'Сохранить', id: 'saveMyInfoBtn'};

//        console.log('user widget');

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>НАСТРОЙКИ</h4>

                        <br/>

                        <form className="form-horizontal" role="form">
                            <UserProperty data={nameProp} onChange={this._onChange}/>
                            <UserProperty data={phoneProp} onChange={this._onChange}/>
                            <UserProperty data={emailProp} onChange={this._onChange}/>

                            <UserButton data={submitButton} onClick={this._onSave}/>
                        </form>

                    </div>
                </div>
            </div>
        );
    }
});