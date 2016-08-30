/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import PropertyPreview from './PropertyPreview.React'

export default class PropertyPreviewListItemWrapper extends Component {
    render() {
        const apartment = this.props.apartment
        console.log("Apartment wrapper for", apartment)

        return (
            <li className="col-lg-4 col-md-6">
                <PropertyPreview apartment={apartment}/>
            </li>
        )
    }
}

PropertyPreviewListItemWrapper.propTypes = {
    apartment: PropTypes.object.isRequired
}

//
// PropertyPreview.contextTypes = {
//     apartment: PropTypes.object.isRequired
// }
