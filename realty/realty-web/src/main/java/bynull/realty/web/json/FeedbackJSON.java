package bynull.realty.web.json;

import bynull.realty.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by dionis on 4/11/15.
 */
@Getter
@Setter
public class FeedbackJSON {
    private Long id;
    private String text;
    private UserJSON creator;
    private Date created;
    private Date updated;
}
