package bynull.realty.dto;

import bynull.realty.data.business.PhoneNumber;
import bynull.realty.data.business.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dionis on 04/12/14.
 */
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String displayName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String facebookId;
    private String vkontakteId;
    private String password;

    public static UserDTO from(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setDisplayName(user.getDisplayName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        PhoneNumber phoneNumber = user.getPhoneNumber();
        dto.setPhoneNumber(phoneNumber != null ? phoneNumber.getRawNumber() : null);
        dto.setFacebookId(user.getFacebookId());
        dto.setVkontakteId(user.getVkontakteId());

        return dto;
    }

    public static User toReference(UserDTO dto) {
        //TODO: add proper implementation later
        if (dto == null) {
            return null;
        }

        User out = new User();

        out.setId(dto.getId());

        return out;
    }
}
