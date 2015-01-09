package bynull.realty.services.metro;

/**
 * Created by null on 13.08.14.
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * {
 * "html_attributions" : [],
 * "results" : [
 * {
 * "formatted_address" : "Ulitsa Podbelskogo, Moscow, Russia, 107370",
 * "geometry" : {
 * "location" : {
 * "lat" : 55.814527,
 * "lng" : 37.734261
 * }
 * },
 * "icon" : "http://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png",
 * "id" : "b2483f401a4621330054425c32e84ce3fcb60b5b",
 * "name" : "Ulitsa Podbelskogo",
 * "place_id" : "ChIJSZ201Vc0tUYRwmlcMFj3S9o",
 * "reference" : "CoQBfQAAAC3-eoq-ea66s-uSXQnG-cTcnUGbG7IN2B3Jqi6ANK9j4jXIYvoKcB3vbSMqPquKDBx0NZXWr-hK6egWo76JIDxU0FzvIirVb2BG9_5I_34Wecsg3SD2wwwYrvuPBXr7-li2Ozy12isbovaQnUw8Q8811nIs5Ym0beASse0Lti17EhCzqWBM-FxvyfuIuvW5oNPJGhT8YnGHvP8Rxyys8h9pxcuqJo4JIQ",
 * "types" : [ "subway_station", "transit_station", "train_station", "establishment" ]
 * }
 * ],
 * "status" : "OK"
 * }
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@ToString
public class GoogleStationInfo {

    @Setter
    @Getter
    @XmlElement(name = "results")
    private List<GoogleStationDetailedInfo> results;

    /*public String getLat(){
        return results.get(0).geometry.location.lat;
    }

    public String getLng(){
        return results.get(0).geometry.location.lng;
    }*/

    @ToString
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.NONE)
    public static class GoogleStationDetailedInfo {

        @Getter
        @Setter
        @XmlElement(name = "formatted_address")
        private String formattedAddress;

        @Getter
        @Setter
        @XmlElement(name = "geometry")
        private GoogleGeometryInfo geometry;

        @ToString
        @XmlRootElement
        @XmlAccessorType(XmlAccessType.NONE)
        public static class GoogleGeometryInfo {

            @Getter
            @Setter
            @XmlElement(name = "location")
            private GoogleLocationDto location;

            @ToString
            @XmlRootElement
            @XmlAccessorType(XmlAccessType.NONE)
            public static class GoogleLocationDto {

                @Getter
                @Setter
                @XmlElement(name = "lat")
                private String lat;

                @Getter
                @Setter
                @XmlElement(name = "lng")
                private String lng;
            }
        }
    }
}
