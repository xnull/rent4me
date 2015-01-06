/**
 * Created by dionis on 06/01/15.
 */

var React = require('react');
var Chats = require('./chats');

module.exports = React.createClass({
    getInitialState: function() {
        return {
            chats: [],
            hasMoreSearchResults: []
        }
    },

    render: function () {
        var items = this.state.chats || [];
        var hasMoreResults = this.state.hasMoreSearchResults || false;

        return (
            <div className="col-md-9">
                <div className="panel">

                    <div className="panel-body">
                        <h4>Сообщения</h4>


                        <form className="form-horizontal" role="form">
                            <div className="form-group">
                                <label className="col-md-2 control-label">Поиск по текущим адресатам</label>
                                <div className="col-md-10">
                                    <input type="text" placeholder="Фильтрация адресатов"/>
                                </div>
                            </div>

                            <div className="col-md-offset-9">
                                <a className="btn btn-primary center-block" >Поиск</a>
                            </div>
                        </form>
                    </div>
                </div>

                <Chats items={items} shown={items.length > 0} hasMore={hasMoreResults} onHasMoreClicked={this.loadMoreResults} />
            </div>
        );
    }
});