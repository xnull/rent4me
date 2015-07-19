var React = require('react');

var AdvertManagement = React.createClass({

    connect: function () {
        return (
            <a href={this.props.item.external_link} className="btn btn-danger center-block"
               style={{padding: '12px'}} target="_blank">
                Связаться
            </a>
        )
    },

    getAuthorLink: function () {
        var link = this.props.item.external_author_link;
        return link ? link : ""
    },

    render: function () {
        var item = this.props.item;

        return (
            <div className="row">
                <div className="col-md-12">
                    <div className="panel panel-default noBorderRadius"
                         style={{marginBottom: '0 !important', border: 'none', borderBottom: '1px solid #e1e2e3'}}>
                        <div className="panel-body">
                            <div className="row">
                                <!-- Фоточка разместившего объяву товарища (зареганного или фотка профиля из соц сети) -->
                                <div className="col-xs-2 col-md-1">
                                    <a href={this.getAuthorLink()} target="_blank">
                                        <!-- super man: http://mediad.publicbroadcasting.net/p/kpcw/files/201404/superman_no_photo.png -->
                                        <img src="https://nslnr.su/pict/no_photo_man.gif" alt="..."
                                             className="img-rounded" width="38"/>
                                    </a>
                                </div>

                                <!-- Конпки связи -->
                                <div className="col-xs-3 col-md-3 ">
                                    {Utils.IfDefined(item.external_link, this.connect)}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = AdvertManagement;