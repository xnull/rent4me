/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import {Link} from 'react-router'

import TopHeader from './TopHeader.React'
import SiteBranding from './SiteBranding.React'
import LoginModal from './LoginModal.React'

export default class Footer extends Component {
    render() {

        return (
            <div>
                <footer className="site-footer" id="footer">

                    <div className="site-footer-top" id="footer-top">
                        <div className="container">
                            <div className="row">
                                <div className="col-sm-12">
                                    <ul className="list-unstyled">
                                        <li className="widget widget_text">
                                            <div className="widget-content"><h5 className="widget-title">Аренда
                                                недвижимости без посредников</h5>
                                                <div className="textwidget"><p>Возможно ли такое в наше время в России?
                                                    Этим вопросом задаются все, кто хоть раз пытался снять квартиру.
                                                    Реалии нашей жизни таковы, что агенства недвижимости захватили рынок
                                                    аренды жилья. Найти квартиру самостоятельно очень сложно.
                                                    Итог: недопустимо низкое качество предоставляемых услуг, от которого
                                                    стардают все кроме самих посредников, которые и являются причиной
                                                    царящего хаоса на рынке.</p>
                                                </div>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="site-footer-bottom" id="footer-bottom">
                        <div className="container">
                            <div className="row">

                                <div className="col-sm-6 footer-bottom-left">
                                    <div className="widget-content">
                                        <div className="textwidget">Copyright © 2014-2016 by Rent4.Me team</div>
                                    </div>
                                </div>

                                {/*Temporarily disable until we have pages where to go
                                <div className="col-sm-6 footer-bottom-right">
                                    <div className="widget-content">
                                        <div className="menu-custom-footer-menu-container">
                                            <ul id="menu-custom-footer-menu" className="menu">
                                                <li
                                                    className="menu-item menu-item-type-custom menu-item-object-custom">
                                                    <Link to="/support">Связаться с нами</Link>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                */}
                            </div>
                        </div>
                    </div>

                </footer>
                <LoginModal />
            </div>
        )
    }
}
