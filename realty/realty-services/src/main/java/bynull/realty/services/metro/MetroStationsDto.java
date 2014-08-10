package bynull.realty.services.metro;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by null on 10.08.14.
 */
@XmlRootElement
@ToString
public class MetroStationsDto {

    @Getter
    @XmlElement(name = "stations")
    private Map<String, MetroStationDto> stations;

    @Getter
    @XmlElement(name = "lines")
    private Map<String, LineOfMetroDto> lines;

    @Getter
    @XmlElement(name = "stationCount")
    private int stationCount;

    @Getter
    @XmlElement(name = "lineCount")
    private int lineCount;

    @JsonAnyGetter
    public Map<String, MetroStationDto> getStations() {
        return ImmutableMap.copyOf(stations);
    }

    @JsonAnySetter
    public void setStations(Map<String, MetroStationDto> stations) {
        this.stations = ImmutableMap.copyOf(stations);
    }

    @JsonAnyGetter
    public Map<String, LineOfMetroDto> getLines() {
        return ImmutableMap.copyOf(lines);
    }

    @JsonAnySetter
    public void setLines(Map<String, LineOfMetroDto> lines) {
        this.lines = ImmutableMap.copyOf(lines);
    }

    /**
     * "1":{"name":"Улица Подбельского","lineId":1,"linkIds":[0],"labelId":1,"isTransferStation":false}
     */
    @XmlRootElement
    @ToString
    public static class MetroStationDto {

        @Getter
        @XmlElement(name = "name")
        private String name;

        @Getter
        @XmlElement
        private int lineId;
    }

    /**
     * "1": {"name": "Сокольническая линия", "color": "#EF1E25", "stationIds": [1, ... , 19], "linkIds": [0, ... 26], "transferStationIds": [6, ... 13]}
     */
    @XmlRootElement
    @ToString
    public static class LineOfMetroDto {

        @Getter
        @XmlElement(name = "name")
        private String name;

        @Getter
        @XmlElement(name = "color")
        private String color;
    }
}