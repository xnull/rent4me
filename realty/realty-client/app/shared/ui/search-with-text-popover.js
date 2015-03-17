var React = require('react');
var Util = require('rent4meUtil');

var ButtonToolbar = require('react-bootstrap/ButtonToolbar');
var Popover = require('react-bootstrap/Popover');
var OverlayTrigger = require('react-bootstrap/OverlayTrigger');
var Button = require('react-bootstrap/Button');

var SearchWithTextPopover = React.createClass({

    render: function () {

        var addButtonEnabled = this.props.addButtonEnabled;
        var onAddButtonClicked = this.props.onAddButtonClicked;

        var style = {};

        if (!addButtonEnabled) {
            style = Util.inactiveUi;
        }

        return (
            <ButtonToolbar>
                <OverlayTrigger trigger="click" placement="bottom" overlay={
                    <Popover title="Поиск по тексту в объявлениях">
                        {this.props.textInput}
                        <button type="button" className="btn btn-default btn-block" onClick={onAddButtonClicked} style={style}>
                            Добавить
                        </button>
                    </Popover>
                    }
                >
                    <Button bsStyle="default">Текст</Button>
                </OverlayTrigger>
            </ButtonToolbar>
        )
    }
});

module.exports = SearchWithTextPopover;