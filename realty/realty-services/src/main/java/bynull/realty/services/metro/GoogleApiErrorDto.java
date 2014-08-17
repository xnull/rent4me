package bynull.realty.services.metro;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * {
 *     "error_message" : "You have exceeded your daily request quota for this API.",
 *     "html_attributions" : [],
 *     "results" : [],
 *     "status" : "OVER_QUERY_LIMIT"
 * }
 */
@XmlRootElement
public class GoogleApiErrorDto {

    @Getter
    @XmlElement(name = "error_message")
    private String errorMessage;

    @Getter
    @XmlElement(name = "status")
    private String status;
}
