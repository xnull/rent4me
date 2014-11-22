/** @jsx React.DOM */

var LeftPaneComponent = React.createClass({
    render: function () {
        var develop = true;

        return (
            <div className="col-md-3">
                <UserComponent/>
                <PersonalCabinetComponent/>
                { this.develop ? <NewsComponent /> : null }
                { this.develop ? <LegalComponent/> : null }
            </div>
        )
    }
});

React.render(<LeftPaneComponent/>, document.getElementById('leftPane'));