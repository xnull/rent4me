var React = require('react');
var JSON2 = require('JSON2');
var AddressUtils = require('../common/AddressUtils');
var assign = require('object-assign');

var AddressBox = React.createClass({

    propTypes: {
        onAddressChange: React.PropTypes.func.isRequired,
        onAddressSelected: React.PropTypes.func.isRequired,
        initialValue: React.PropTypes.any
    },

    getInitialState: function () {
        return this.props.initialValue || {};
    },

    componentDidMount: function () {
        this.registerGoogleAutoComplete(this);
    },

    //commented out because it will lead to blinking
    /*
    componentDidUpdate: function () {
        this.registerGoogleAutoComplete(this);
    },
    */

    /**
     * Should be called after initial rendering and after each update in order to work
     */
    registerGoogleAutoComplete(component){


        var domElement = component.getDOMNode();

        var that = this;
        var autocomplete = new google.maps.places.Autocomplete(domElement);
        this._autocomplete = autocomplete;

        this._listener = google.maps.event.addListener(autocomplete, 'place_changed', function () {
            var place = autocomplete.getPlace();
            if (!place) return;
            var dump = JSON2.stringify(place);
            console.log('dump:');
            console.log(dump);
            var addressComponents = place['address_components'];

            var viewPort = place.geometry.viewport;

            var bounds = viewPort ? {
                northEast: {
                    lat: viewPort.getNorthEast().lat(),
                    lng: viewPort.getNorthEast().lng()
                },

                southWest: {
                    lat: viewPort.getSouthWest().lat(),
                    lng: viewPort.getSouthWest().lng()
                }
            } : null;

            var countryCode = AddressUtils.getAddressComponentOfTypeOrNull(addressComponents, 'COUNTRY');

            var location = {
                latitude: place['geometry']['location'].lat(),
                longitude: place['geometry']['location'].lng()
            };

            var formatted_address = place['formatted_address'];
            var name = place['name'];

            console.log('new location:');
            console.log(location);

            that.setState(assign(that.state, {
                location: location,
                countryCode: countryCode,
                bounds: bounds,
                formattedAddress: formatted_address,
                formattedName: name
            }));
            that.props.onAddressSelected(that.state);
        });
    },

    makeFormattedAddressDirty: function (value) {
        this.setState(assign(this.state, {
            location: null,
            countryCode: null,
            bounds: null,
            formattedName: value,
            formattedAddress: null
        }));
    },

    onAddressChange: function(e){
        console.log('google component on address change: '+e.target.value);
        this.makeFormattedAddressDirty(e.target.value);
        //this.props.onAddressChange(e);
    },

    //commented out because it's not working properly
/*
    onBoxLeave: function() {
        console.log('google component on box leave');
        console.log('Current state of google address component: '+JSON2.stringify(this.state));

        if(!this.state.location) {
            console.log('no location available');
            this.setState(assign(this.state, {
                location: null,
                countryCode: null,
                bounds: null,
                formattedName: null,
                formattedAddress: null,
                error: true
            }));
        }

        this.props.onBoxLeave(this.state);
    },
*/
    render: function () {
        console.log('(re-)rendering google component');
        var displayValue = this.state.formattedName;

        var dump = JSON2.stringify(this.state);

        console.log('Current state of google address component on (re-)render : '+dump);
        console.log('Following will be displayed : "'+displayValue+'"');

        var error = this.state.error;


        if(error) {
            //TODO: show visually that something changed
        }

        var result = (

                <input
                    className="form-control"
                    type="text"
                    value={displayValue}
                    style={{
                        paddingTop: 0,
                        paddingBottom: 0,
                        borderRadius: 0
                    }}
                    placeholder="Введите город/район/метро"
                    onChange={this.onAddressChange}
                />

        );



        return (
            result
        )
    }
});

module.exports = AddressBox;