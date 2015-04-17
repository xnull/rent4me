var React = require('react');
var Util = require('rent4meUtil');

var ButtonToolbar = require('react-bootstrap/ButtonToolbar');
var Popover = require('react-bootstrap/Popover');
var OverlayTrigger = require('react-bootstrap/OverlayTrigger');
var Button = require('react-bootstrap/Button');

var MetroPopover = React.createClass({

    propTypes: {
        metroInput: React.PropTypes.any.isRequired,
        onAddButtonClicked: React.PropTypes.func.isRequired
    },

    render: function () {

        var enabled = this.props.addButtonEnabled;

        var style = {};

        if (!enabled) {
            style = Util.inactiveUi;
        }

        var display = null;

        if(this.props.metroInput) {
            display = this.props.metroInput;
        } else {
            display = (
                <div className="alert alert-info">
                    Пожалуйста выберите в строке поиска интересующий вас город и мы сможем отобразить список метро этого города.
                </div>
            )
        }


        return (
            <ButtonToolbar>
                <OverlayTrigger trigger="click" placement="bottom" overlay={
                    <Popover title="Поиск по станциям метро">
                        {display}
                        <button type="button" className="btn btn-default btn-block" onClick={this.props.onAddButtonClicked} style={style}>
                            Выбрать
                        </button>
                    </Popover>
                    }
                >
                    <Button bsStyle="default">Метро</Button>
                </OverlayTrigger>
            </ButtonToolbar>
        )
    }
});

module.exports = MetroPopover;