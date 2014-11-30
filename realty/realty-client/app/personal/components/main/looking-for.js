var AddressBox = React.createClass({
    render: function () {
        return (
            <input
                id="addressInput"
                className="form-control"
                type="text"
                placeholder="Введите местоположение"
                value={this.props.data.address}
                onChange={this.onAddressChange}
            />
        )
    },

    onAddressChange: function (event) {
        this.props.onChange(event.target.value);
    }
});

var News = React.createClass({
    render: function () {
        return (
            <div className="col-md-9">
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
                                        <img src="http://api.randomuser.me/portraits/med/men/4.jpg" width="32px" height="28px"/>
                                    </p>
                                </a>

                                <br/>

                                <NewsItem/>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

var NewsItem = React.createClass({
    render: function () {
        return (
            <a href="#" className="list-group-item">
                <h4 className="list-group-item-heading">Второе событие</h4>

                <p className="list-group-item-text">Ничто не вечно под луной</p>
            </a>
        );
    }
});

module.exports = React.createClass({
    /**
     * https://developers.google.com/places/?hl=ru
     * https://developers.google.com/maps/documentation/javascript/examples/places-autocomplete?hl=ru
     *
     * @param rootNode
     */
    componentDidMount: function (rootNode) {
        var autocomplete = new google.maps.places.Autocomplete(document.getElementById('addressInput'));
    },

    render: function () {
        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Мне интересно</h4>


                        <form className="form-horizontal" role="form">
                            <div className="form-group">
                                <label className="col-md-2 control-label">Адрес</label>
                                <div className="col-md-10">
                                    <AddressBox
                                        data={this.state !== null ? this.state.data : this.props.data}
                                        onChange={this.handleAddressChange}
                                        ref = "addressInput"
                                    />
                                </div>
                            </div>

                            <div className="col-md-offset-9">
                                <a className="btn btn-primary center-block" onClick={this.onClick}>Поиск</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );
    },

    handleAddressChange: function (address) {
        this.setState({
            data: {address: address}
        });
    },

    onClick: function () {
        this.props.onSearchNews(
            {
                address: this.refs.addressInput.getDOMNode().value
            }
        );
    }
});

