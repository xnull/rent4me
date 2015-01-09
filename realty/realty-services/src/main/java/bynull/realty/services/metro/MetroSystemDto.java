package bynull.realty.services.metro;

import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import static bynull.realty.services.metro.MetroStationsDto.MetroStationDto;

/**
 * Created by null on 16.08.14.
 */
@XmlRootElement
@NoArgsConstructor
public class MetroSystemDto {

    @XmlElement(name = "metro_system")
    private List<MetroStationFullInfoDto> stations = new ArrayList<>();

    @Getter
    @Setter
    @XmlElement(name = "city")
    private String city;

    public List<MetroStationFullInfoDto> getStations() {
        return new ArrayList<>(stations);
    }

    @XmlRootElement
    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class MetroStationFullInfoDto {
        @XmlElement(name = "coords")
        private GoogleStationInfo coords;

        @XmlElement(name = "station")
        private MetroStationDto station;
    }

    public void addStation(MetroStationFullInfoDto station) {
        stations.add(station);
    }
}
