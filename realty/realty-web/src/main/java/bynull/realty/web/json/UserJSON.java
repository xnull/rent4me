package bynull.realty.web.json;

import bynull.realty.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dionis on 04/12/14.
 */
@Getter
@Setter
public class UserJSON {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("name")
    private String displayName;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phoneNumber;
    @JsonProperty("fb_id")
    private String facebookId;
    @JsonProperty("vk_id")
    private String vkontakteId;
    @JsonProperty("password")
    private String password;

    public static UserJSON from(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        UserJSON json = new UserJSON();

        json.setId(dto.getId());
        json.setDisplayName(dto.getDisplayName());
        json.setFirstName(dto.getFirstName());
        json.setLastName(dto.getLastName());
        json.setEmail(dto.getEmail());
        json.setPhoneNumber(dto.getPhoneNumber());
        json.setFacebookId(dto.getFacebookId());
        json.setVkontakteId(dto.getVkontakteId());

        return json;
    }

    public UserDTO toDTO() {
        UserDTO dto = new UserDTO();

        dto.setId(getId());
        dto.setDisplayName(getDisplayName());
        dto.setFirstName(getFirstName());
        dto.setLastName(getLastName());
        dto.setEmail(getEmail());
        dto.setPhoneNumber(getPhoneNumber());
        dto.setFacebookId(getFacebookId());
        dto.setVkontakteId(getVkontakteId());
        dto.setPassword(getPassword());

        return dto;
    }
}
