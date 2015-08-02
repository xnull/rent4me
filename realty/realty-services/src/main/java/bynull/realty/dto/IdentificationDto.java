package bynull.realty.dto;

import lombok.Value;

import java.util.Optional;

/**
 * Created by null on 8/1/15.
 */
@Value
public class IdentificationDto {
    private Long id;
    private Optional<Long> userId;
    private Optional<Long> facebookId;
    private Optional<Long> vkontakteId;
    private Optional<Long> phoneNumber;
    private Optional<String> email;
}
