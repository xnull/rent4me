/** @jsx React.DOM */

var LookingForComponent = React.createClass({
    componentDidMount: function (rootNode) {
        var input = /** @type {HTMLInputElement} */(
            document.getElementById('pac-input'));

        var autocomplete = new google.maps.places.Autocomplete(input);
        //autocomplete.bindTo('bounds', map);
    },

    render: function () {
        return (
            <div className="panel">

                <div className="panel-body">
                    <h4>Мне интересно</h4>


                        <form className="form-horizontal" role="form">

                            <div className="form-group">
                                <label className="col-md-2 control-label">Адрес</label>
                                <div className="col-md-10">
                                    <input id="pac-input" className="form-control" type="text" placeholder="Введите местоположение"/>
                                </div>
                            </div>

                            <div className="col-md-offset-8">
                                <a className="btn btn-primary center-block" href="#">Поиск</a>
                            </div>
                        </form>
                    </div>

            </div>
        );
    }
});

React.render(
    <LookingForComponent />
    ,
    document.getElementById('lookingForComponent')
);