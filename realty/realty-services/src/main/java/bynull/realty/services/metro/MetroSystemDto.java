package bynull.realty.services.metro;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import static bynull.realty.services.metro.MetroStationsDto.MetroStationDto;

/**
 * Created by null on 16.08.14.
 */
@ToString
@XmlRootElement
public class MetroSystemDto {

    @Getter
    @XmlElement(name = "metro_system")
    private List<MetroStationFullInfoDto> stations = new ArrayList<>();

    @ToString
    @XmlRootElement
    public static class MetroStationFullInfoDto {
        @Setter
        @Getter
        @XmlElement(name = "coords")
        private GoogleStationInfo coords;

        @Setter
        @Getter
        @XmlElement(name = "station")
        private MetroStationDto station;

        public MetroStationFullInfoDto(GoogleStationInfo coords, MetroStationDto station) {
            this.coords = coords;
            this.station = station;
        }

        /**
         * Is needed for JAXB
         */
        private MetroStationFullInfoDto() {
        }
    }

    public void addStation(MetroStationFullInfoDto station) {
        stations.add(station);
    }
}
