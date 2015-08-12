package bynull.realty.converters.apartments;

import bynull.realty.data.business.FacebookApartment;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
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
public class FacebookApartmentModelDTOConverter extends SocialNetApartmentModelDTOConverter<FacebookApartment> {
    @Override
    public Optional<ApartmentDTO> toTargetType(Optional<FacebookApartment> apartment, ApartmentDTO dto) {
        log.trace("Convert fb apartment to dto: ", apartment);
        return super.toTargetType(apartment, dto).flatMap(apartmentDTO -> {
            FacebookApartment apt = apartment.get();
            FacebookPageToScrap facebookPage = apt.getFacebookPage();
            CityEntity city = facebookPage.getCity();
            if (city != null) {
                apartmentDTO.setCity(city.getName());
            }

            Optional.ofNullable(apt.getExtAuthorLink()).ifPresent(link -> {
                apartmentDTO.setAuthorId(parseAuthorId(link));
            });

            return Optional.of(apartmentDTO);
        });
    }

    /**
     * @param authorLink for example: https://www.facebook.com/131345607204302
     * @return
     */
    public static String parseAuthorId(String authorLink) {
        return authorLink.substring(authorLink.lastIndexOf("/") + 1);
    }
}
