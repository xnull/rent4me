package bynull.realty.services.metro;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by null on 10.08.14.
 */
@XmlRootElement(name = "svg")
@ToString
public class SvgMetro {

    @XmlElement(name = "metadata")
    @Getter
    private SvgMetroMetaData metaData;

    @XmlRootElement
    @ToString
    public static class SvgMetroMetaData {

        @XmlValue
        @Getter
        private String metadata;
    }
}
