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

        return (
            <ButtonToolbar>
                <OverlayTrigger trigger="click" placement="bottom" overlay={
                    <Popover title="Поиск по станциям метро">
                        {this.props.metroInput}
                        <button type="button" className="btn btn-default btn-block" onClick={this.props.onAddButtonClicked} style={style}>
                            Добавить
                        </button>
                    </Popover>
                    }
                >
                    <Button bsStyle="default" style={style}>Метро</Button>
                </OverlayTrigger>
            </ButtonToolbar>
        )
    }
});

module.exports = MetroPopover;