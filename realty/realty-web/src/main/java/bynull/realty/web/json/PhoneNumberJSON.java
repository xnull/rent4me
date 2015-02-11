package bynull.realty.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 2/11/15.
 */
@Getter
@Setter
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class PhoneNumberJSON {
    @JsonProperty("raw_number")
    private String rawNumber;
    @JsonProperty("national_formatted_number")
    private String nationalFormattedNumber;
}
