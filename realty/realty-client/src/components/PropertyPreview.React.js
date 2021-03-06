/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import SmartLink from './SmartLink.React'
import moment from 'moment'

export default class PropertyPreview extends Component {
    render() {
        const apartment = this.props.apartment

        // const linkLocation = "/apartments/" + apartment.id
        //temporary workaround until property page will be done
        const linkLocation = apartment.external_link ?
            apartment.external_link
            : "/apartments/" + apartment.id

        const imgUrl = (apartment.external_images.length > 0
            ? apartment.external_images[0].full_picture_url
            : '/images/no-propertyfound.png')

        return (
            <div className="property-item border-box  featured">

                <SmartLink to={linkLocation}>
                    <figure className="property-thumbnail">
                        <img width="600" height="300"
                             src={imgUrl}
                             className="propertyPreviewImage wp-post-image" alt=""/>
                        <figcaption>
                            <div className="property-excerpt">
                                {apartment.address.formatted_address? <h4 className="address">{apartment.address.formatted_address}</h4> : ''}
                                <p>{apartment.description}</p>
                            </div>
                            {
                                apartment.published
                                    ?
                                    (<div className="property-tag tag-left">
                                        Добавлено {moment(apartment.created).format('lll')}
                                    </div>)
                                    : <div className="property-tag tag-left">
                                    Объявление неактивно
                                </div>
                            }
                        </figcaption>
                    </figure>
                </SmartLink>

                <div className="property-content content">
                    <div className="property-title">
                        <SmartLink to={linkLocation}>
                            <h3 className="title">{apartment.address.formatted_address ? apartment.address.formatted_address: ''}</h3></SmartLink>
                    </div>
                    <div className="property-meta clearfix">
                        {/*<div>
                            <div className="meta-title"><i className="icon-size"></i></div>
                            <div className="meta-data" data-toggle="tooltip" title="" data-original-title="Площадь">
                                1500 м<sup>2</sup>
                            </div>
                        </div>*/}
                        {
                            apartment.room_count
                                ? (
                                <div>
                                    <div className="meta-title"><i className="icon-rooms"></i></div>
                                    <div className="meta-data" data-toggle="tooltip" title="" data-original-title="Комнат">Комнат: {apartment.room_count}</div>
                                </div>
                                ) : (<div>&nbsp;</div>)
                        }
                    </div>


                    <div className="property-price">

                        <div className="price-tag">
                            { apartment.rental_fee ? ( <span>{apartment.rental_fee} рублей {([apartment.fee_period].map(period=> {
                            switch ((period || '')) {
                                case 'HOURLY':
                                    return "в час";
                                case 'DAILY':
                                    return "в день";
                                case 'WEEKLY':
                                    return "в неделю";
                                case 'MONTHLY':
                                    return "в месяц";
                            }
                        }))}</span>):(<span>&nbsp;</span>)}
                        </div>
                        {/*
                        <div className="property-icons">
									<span style={{position: 'relative'}}>
                                        <div className="share-unit" style={{display: 'none'}}>
                                            <a className="social-facebook" target="_blank"
                                               href="http://www.facebook.com/sharer.php?u=http%3A%2F%2Fdemo.themetrail.com%2Frealty%2Fproperty%2Ffuturistic-nest%2F&amp;t=Futuristic+Nest"><i
                                                className="icon-facebook"></i></a>
                                            <a target="_blank" className="social-twitter"
                                               href="http://twitter.com/home?status=Futuristic+Nest+http%3A%2F%2Fdemo.themetrail.com%2Frealty%2Fproperty%2Ffuturistic-nest%2F"><i
                                                className="icon-twitter"></i></a>
                                            <a className="social-google" target="_blank"
                                               href="https://plus.google.com/share?url=http://demo.themetrail.com/realty/property/futuristic-nest/"><i
                                                className="icon-google-plus"></i></a>
                                            <a className="social-pinterest" target="_blank"
                                               href="http://pinterest.com/pin/create/button/?url=http://demo.themetrail.com/realty/property/futuristic-nest/&amp;description=http%3A%2F%2Fdemo.themetrail.com%2Frealty%2Fproperty%2Ffuturistic-nest%2F"><i
                                                className="icon-pinterest"></i></a>
                                        </div>
                                        <i className="icon-share share-property" data-toggle="tooltip" data-original-title="Поделиться"
                                           title=""></i>
                                    </span>

                            <i className="add-to-favorites icon-heart-1" data-fav-id="142" data-toggle="tooltip"
                               title="" data-original-title="В избранное"></i><a href="//vimeo.com/63241912"
                                                                                 className="property-video-popup"><i
                            className="icon-video-camera" data-toggle="tooltip" title=""
                            data-original-title="Видео превью"></i></a>

                        </div>*/}
                        <div className="clearfix"></div>

                    </div>

                </div>
            </div>
        )
    }
}

PropertyPreview.propTypes = {
    apartment: PropTypes.object.isRequired
}

//
// PropertyPreview.contextTypes = {
//     apartment: PropTypes.object.isRequired
// }
