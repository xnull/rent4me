/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'

export default class PropertyPreview extends Component {
    render() {
        const apartment = this.props.apartment

        return (
            <div className="property-item border-box  featured">

                <a href="http://demo.themetrail.com/realty/property/futuristic-nest/">
                    <figure className="property-thumbnail">
                        <img width="600" height="300"
                             src={apartment.previewImage}
                             className=" wp-post-image" alt="One Story House"/>
                        <figcaption>
                            <div className="property-excerpt">
                                <h4 className="address">{apartment.address}</h4>
                                <p>{apartment.description}</p>
                            </div>
                            {
                                apartment.rented
                                    ?
                                    (<div className="property-tag tag-left">
                                        Rented Out
                                    </div>)
                                    : ''
                            }
                        </figcaption>
                    </figure>
                </a>

                <div className="property-content content">
                    <div className="property-title">
                        <a href="http://demo.themetrail.com/realty/property/futuristic-nest/">
                            <h3 className="title">Futuristic Nest</h3></a>
                    </div>
                    <div className="property-meta clearfix">
                        <div>
                            <div className="meta-title"><i className="icon-size"></i></div>
                            <div className="meta-data" data-toggle="tooltip" title="" data-original-title="Size">
                                1500 sq ft
                            </div>
                        </div>
                        <div>
                            <div className="meta-title"><i className="icon-rooms"></i></div>
                            <div className="meta-data" data-toggle="tooltip" title="" data-original-title="Rooms">8
                                Rooms
                            </div>
                        </div>
                        <div>
                            <div className="meta-title"><i className="icon-bedrooms"></i></div>
                            <div className="meta-data" data-toggle="tooltip" title="" data-original-title="Bedrooms">3
                                Bedrooms
                            </div>
                        </div>
                        <div>
                            <div className="meta-title"><i className="icon-bathrooms"></i></div>
                            <div className="meta-data" data-toggle="tooltip" title="" data-original-title="Bathrooms">2
                                Bathrooms
                            </div>
                        </div>
                    </div>


                    <div className="property-price">

                        <div className="price-tag">
                            $5,000&nbsp;per month
                        </div>

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
						<i className="icon-share share-property" data-toggle="tooltip" data-original-title="Share"
                           title=""></i>
					</span>

                            <i className="add-to-favorites icon-heart-1" data-fav-id="142" data-toggle="tooltip"
                               title="" data-original-title="Add To Favorites"></i><a href="//vimeo.com/63241912"
                                                                                      className="property-video-popup"><i
                            className="icon-video-camera" data-toggle="tooltip" title=""
                            data-original-title="Watch Trailer"></i></a><i className="icon-add compare-property"
                                                                           data-compare-id="142" data-toggle="tooltip"
                                                                           title="" data-original-title="Compare"></i>

                        </div>
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
