package bynull.realty.converters.apartments;

import bynull.realty.data.business.FacebookApartment;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.data.common.CityEntity;
import bynull.realty.dto.ApartmentDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class FacebookApartmentModelDTOConverter extends SocialNetApartmentModelDTOConverter<FacebookApartment> {
    @Override
    public ApartmentDTO toTargetType(FacebookApartment apartment, ApartmentDTO dto) {
        ApartmentDTO apartmentDTO = super.toTargetType(apartment, dto);
        if (apartmentDTO != null) {
            FacebookPageToScrap facebookPage = apartment.getFacebookPage();
            CityEntity city = facebookPage.getCity();
            if (city != null) {
                apartmentDTO.setCity(city.getName());
            }

            Optional.ofNullable(apartment.getExtAuthorLink()).ifPresent(link -> {
                apartmentDTO.setAuthorId(parseAuthorId(link));
            });
        }

        return apartmentDTO;
    }

    /**
     * @param authorLink for example: https://www.facebook.com/131345607204302
     * @return
     */
    static String parseAuthorId(String authorLink) {
        return authorLink.substring(authorLink.lastIndexOf("/") + 1);
    }
}
