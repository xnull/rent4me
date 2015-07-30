package bynull.realty.converters.apartments;

import bynull.realty.data.business.VkontakteApartment;
import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import bynull.realty.data.common.CityEntity;
import bynull.realty.dto.ApartmentDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class VkontakteApartmentModelDTOConverter extends SocialNetApartmentModelDTOConverter<VkontakteApartment> {
    /**
     * @param authorLink https://vk.com/id248324164
     * @return
     */
    static String parseAuthorId(String authorLink) {
        return authorLink.substring(authorLink.lastIndexOf("/") + 1);
    }

    @Override
    public Optional<ApartmentDTO> toTargetType(Optional<VkontakteApartment> apartment, ApartmentDTO dto) {
        return apartment.flatMap(ap -> {
            return super.toTargetType(apartment, dto).flatMap(a -> {
                VkontaktePage vkontaktePage = ap.getVkontaktePage();
                CityEntity city = vkontaktePage.getCity();
                if (city != null) {
                    a.setCity(city.getName());
                }

                a.setAuthorId(parseAuthorId(ap.getExtAuthorLink()));

                return Optional.of(a);
            });
        });
    }
}
