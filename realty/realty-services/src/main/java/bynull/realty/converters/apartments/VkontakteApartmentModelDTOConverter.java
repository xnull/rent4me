package bynull.realty.converters.apartments;

import bynull.realty.data.business.VkontakteApartment;
import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import bynull.realty.data.common.CityEntity;
import bynull.realty.dto.ApartmentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 3/5/15.
 */
@Component
@Slf4j
public class VkontakteApartmentModelDTOConverter extends SocialNetApartmentModelDTOConverter<VkontakteApartment> {
    /**
     * @param authorLink https://vk.com/id248324164
     * @return
     */
    static Optional<String> parseAuthorId(String authorLink) {
        if (authorLink == null){
            return Optional.empty();
        }
        try {
            return Optional.of(authorLink.substring(authorLink.lastIndexOf("/") + 1));
        }
        catch (Exception e){
            log.warn("Unable to parse authorId from url: " + authorLink);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ApartmentDTO> toTargetType(Optional<VkontakteApartment> apartment, ApartmentDTO dto) {
        log.trace("Convert vk apartment to dto: {}", apartment);
        return apartment.flatMap(ap -> {
            return super.toTargetType(apartment, dto).flatMap(a -> {
                VkontaktePage vkontaktePage = ap.getVkontaktePage();
                CityEntity city = vkontaktePage.getCity();
                if (city != null) {
                    a.setCity(city.getName());
                }

                parseAuthorId(ap.getExtAuthorLink()).ifPresent(a::setAuthorId);

                return Optional.of(a);
            });
        });
    }
}
