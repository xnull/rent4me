var React = require('react');
var LookingForComponent = require('./looking-for.js');
var NavActions = require('../../../shared/actions/NavActions');

module.exports = React.createClass({
    componentDidMount: function () {
        NavActions.navigateToHome();
    },

    render: function () {
        return (
            <div>
                <LookingForComponent data={this.state.data} onSearchNews={this.handleSearchNews}/>
                { this.state.showResults ? <News/> : null }
            </div>
        )
    },

    getInitialState: function () {
        console.log("News initial state");

        return {
            showResults: false,
            data: {address: ''}
        };
    },

    handleSearchNews: function (searchNewsRequest) {
        this.setState({
            showResults: true
        });
        this.getNews();
    },

    fakeData: function (searchNewsRequest) {
        return {
            address: searchNewsRequest.address
        }
    },

    /**
     * Get news from the server
     */
    getNews: function () {
        $.ajax({
            url: 'http://maps.googleapis.com/maps/api/js?v=3.exp&libraries=places',
            dataType: 'html',
            success: function (data) {
                console.log("success");
                console.log(data);
                this.setState({data: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(status);
                console.error(xhr);
                console.error(err);
            }.bind(this)
        });
    }
});