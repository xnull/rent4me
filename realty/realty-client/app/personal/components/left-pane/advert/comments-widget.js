/**
 * Created by null on 7/18/15.
 */

var React = require('react');

var CommentsWidget = React.createClass({
    render: function () {
        return (
            <div className="row">
                <div className="col-md-12">
                    <div className="panel panel-default noBorderRadius">
                        <table className="table table-responsive table-striped table-borderless">
                            <tbody>
                            <tr>
                                <td className="col-md-1 text-center">
                                    <a href="#">
                                        <img src="https://nslnr.su/pict/no_photo_man.gif" alt="..."
                                             className="img-circle" width="32"/>
                                    </a>
                                </td>
                                <td>
                                    <form>
                                        <div className="form-group">
                                            <label for="commentSystem">Ваш комментарий</label>
                                            <input type="text" className="form-control"
                                                   id="commentSystem"
                                                   placeholder="текст комментария"/>
                                        </div>
                                    </form>
                                </td>
                            </tr>
                            </tbody>
                        </table>

                        <div className="list-group">
                            <a href="javascript:void(0);" className="list-group-item comment-group-item">

                                <div className="media">
                                    <div className="pull-left">
                                        <img
                                            src="http://geedmo.com/themeforest/wintermin/app/img/user/01.jpg"
                                            alt="Image"
                                            className="media-object img-circle thumb48"/>
                                    </div>

                                    <div className="media-body clearfix">
                                        <small className="pull-right">2h</small>
                                        <strong className="media-heading text-primary">
                                                            <span
                                                                className="point point-success point-lg text-left"></span>Sheila
                                            Carter</strong>

                                        <p className="mb-sm">
                                            <small>Cras sit amet nibh libero, in gravida nulla.
                                                Nulla...
                                            </small>
                                        </p>
                                    </div>
                                </div>
                            </a>
                            <a href="javascript:void(0);"
                               className="list-group-item comment-group-item">

                                <div className="media">
                                    <div className="pull-left">
                                        <img
                                            src="http://geedmo.com/themeforest/wintermin/app/img/user/04.jpg"
                                            alt="Image"
                                            className="media-object img-circle thumb48"/>
                                    </div>

                                    <div className="media-body clearfix">
                                        <small className="pull-right">3h</small>
                                        <strong className="media-heading text-primary">
                                                                <span
                                                                    className="point point-success point-lg text-left"></span>Rich
                                            Reynolds</strong>

                                        <p className="mb-sm">
                                            <small>Cras sit amet nibh libero, in gravida
                                                nulla.
                                                Nulla...
                                            </small>
                                        </p>
                                    </div>
                                </div>
                            </a>
                            <a href="javascript:void(0);"
                               className="list-group-item comment-group-item">
                                <div className="media">
                                    <div className="pull-left">
                                        <img
                                            src="http://geedmo.com/themeforest/wintermin/app/img/user/03.jpg"
                                            alt="Image"
                                            className="media-object img-circle thumb48"/>
                                    </div>

                                    <div className="media-body clearfix">
                                        <small className="pull-right">4h</small>
                                        <strong className="media-heading text-primary">
                                                                    <span
                                                                        className="point point-danger point-lg text-left"></span>Beverley
                                            Pierce</strong>

                                        <p className="mb-sm">
                                            <small>Cras sit amet nibh libero, in gravida
                                                nulla.
                                                Nulla...
                                            </small>
                                        </p>
                                    </div>
                                </div>
                            </a>
                            <a href="javascript:void(0);"
                               className="list-group-item comment-group-item">
                                <div className="media">
                                    <div className="pull-left">
                                        <img
                                            src="http://geedmo.com/themeforest/wintermin/app/img/user/06.jpg"
                                            alt="Image"
                                            className="media-object img-circle thumb48"/>
                                    </div>

                                    <div className="media-body clearfix">
                                        <small className="pull-right">4h</small>
                                        <strong
                                            className="media-heading text-primary">
                                                                        <span
                                                                            className="point point-danger point-lg text-left"></span>Alex
                                            Somar</strong>

                                        <p className="mb-sm">
                                            <small>Vestibulum pretium aliquam
                                                scelerisque.
                                            </small>
                                        </p>
                                    </div>
                                </div>
                            </a>
                        </div>
                        <div className="panel-footer clearfix">
                            <a href="javascript:void(0);" className="pull-left">
                                <small>Показать се комментарии</small>
                            </a>
                        </div>
                    </div>

                </div>
            </div>
        )
    }
});

module.exports = CommentsWidget;