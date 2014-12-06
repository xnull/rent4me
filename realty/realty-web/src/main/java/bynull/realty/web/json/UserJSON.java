package bynull.realty.web.json;

import bynull.realty.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * @author dionis on 04/12/14.
 */
public class UserJSON {
    public static UserJSON from(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        UserJSON json = new UserJSON();

        json.setId(json.getId());
        json.setDisplayName(dto.getDisplayName());
        json.setFirstName(dto.getFirstName());
        json.setLastName(dto.getLastName());
        json.setEmail(dto.getEmail());
        json.setPhoneNumber(dto.getPhoneNumber());
        json.setFacebookId(dto.getFacebookId());
        json.setVkontakteId(dto.getVkontakteId());

        return json;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getVkontakteId() {
        return vkontakteId;
    }

    public void setVkontakteId(String vkontakteId) {
        this.vkontakteId = vkontakteId;
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

        return dto;
    }
}
