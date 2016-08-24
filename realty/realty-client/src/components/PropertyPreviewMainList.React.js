/**
 * Created by dionis on 24.08.16.
 */
import React, {Component, PropTypes} from 'react'
import PropertyPreviewListItemWrapper from './PropertyPreviewListItemWrapper.React'

export default class PropertyPreviewMainList extends Component {
    render() {
        const apartments = this.props.apartments

        const apartmentsDOM = apartments.map( apartment =>
          (
            <PropertyPreviewListItemWrapper key={apartment.id} apartment={apartment}/>
          )
        )

        return (<ul className="row list-unstyled">{apartmentsDOM}</ul>)
    }
}

PropertyPreviewMainList.propTypes = {
    apartments: PropTypes.array.isRequired
}

//
// PropertyPreview.contextTypes = {
//     apartment: PropTypes.object.isRequired
// }
