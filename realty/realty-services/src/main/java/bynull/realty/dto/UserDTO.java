package bynull.realty.dto;

import bynull.realty.data.business.User;

/**
 * @author dionis on 04/12/14.
 */
public class UserDTO {
    public static UserDTO from(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();

        dto.setId(dto.getId());
        dto.setDisplayName(user.getDisplayName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setFacebookId(user.getFacebookId());
        dto.setVkontakteId(user.getVkontakteId());

        return dto;
    }

    private Long id;
    private String username;
    private String displayName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String facebookId;
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
}
