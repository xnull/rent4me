package bynull.realty.dto;

import bynull.realty.data.business.Feedback;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by dionis on 4/11/15.
 */
@Getter
@Setter
public class FeedbackDTO {
    private Long id;
    private String text;
    private String name;
    private String email;
    private UserDTO creator;
    private Date created;
    private Date updated;
}
