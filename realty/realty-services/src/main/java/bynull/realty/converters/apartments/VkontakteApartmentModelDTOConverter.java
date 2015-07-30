package bynull.realty.converters.apartments;

import bynull.realty.data.business.VkontakteApartment;
import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import bynull.realty.data.common.CityEntity;
import bynull.realty.dto.ApartmentDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class VkontakteApartmentModelDTOConverter extends SocialNetApartmentModelDTOConverter<VkontakteApartment> {
    @Override
    public ApartmentDTO toTargetType(VkontakteApartment apartment, ApartmentDTO dto) {
        ApartmentDTO apartmentDTO = super.toTargetType(apartment, dto);
        if (apartmentDTO != null) {
            VkontaktePage vkontaktePage = apartment.getVkontaktePage();
            CityEntity city = vkontaktePage.getCity();
            if (city != null) {
                apartmentDTO.setCity(city.getName());
            }

            apartmentDTO.setAuthorId(parseAuthorId(apartment.getExtAuthorLink()));
        }
        return apartmentDTO;
    }

    /**
     *
     * @param authorLink https://vk.com/id248324164
     * @return
     */
    static String parseAuthorId(String authorLink){
        return authorLink.substring(authorLink.lastIndexOf("/") + 1);
    }
}
