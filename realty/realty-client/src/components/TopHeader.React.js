/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import {Link} from 'react-router'

export default class TopHeader extends Component {
    render() {

        return (
            <div className="top-header">
                <div className="container">
                    <div className="top-header-sidebar"></div>

                    <div className="top-header-links primary-tooltips">


                        <Link to="/apartments/new">Добавить квартиру/комнату</Link>

                        <a href="#login-modal" data-toggle="modal">Войти</a>

                    </div>
                </div>
            </div>
        )
    }
}
