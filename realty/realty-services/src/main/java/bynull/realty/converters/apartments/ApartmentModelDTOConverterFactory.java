package bynull.realty.converters.apartments;

import bynull.realty.common.ConverterFactory;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.FacebookApartment;
import bynull.realty.data.business.InternalApartment;
import bynull.realty.data.business.VkontakteApartment;
import bynull.realty.dto.ApartmentDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class ApartmentModelDTOConverterFactory<S extends Apartment> implements ConverterFactory<S, ApartmentDTO, ApartmentModelDTOConverter<S>> {
    private final ApartmentModelDTOConverter<S> NULL_CONVERTER = new ApartmentModelDTOConverter<S>() {
        @Override
        public ApartmentDTO newTargetType(Optional<S> in) {
            return null;
        }

        @Override
        public S newSourceType(ApartmentDTO in) {
            return null;
        }

        @Override
        public Optional<ApartmentDTO> toTargetType(Optional<S> in, ApartmentDTO instance) {
            return Optional.empty();
        }

        @Override
        public S toSourceType(ApartmentDTO in, S instance) {
            return null;
        }
    };
    @Resource
    InternalApartmentModelDTOConverter internalApartmentModelDTOConverter;

    @Resource
    FacebookApartmentModelDTOConverter facebookApartmentModelDTOConverter;

    @Resource
    VkontakteApartmentModelDTOConverter vkontakteApartmentModelDTOConverter;

    @Override
    public ApartmentModelDTOConverter<S> getTargetConverter(S in) {
        if (in == null) {
            return NULL_CONVERTER;
        }

        switch (in.getType()){
            case INTERNAL:
                return (ApartmentModelDTOConverter<S>) internalApartmentModelDTOConverter;
            case FB:
                return (ApartmentModelDTOConverter<S>) facebookApartmentModelDTOConverter;
            case VK:
                return (ApartmentModelDTOConverter<S>) vkontakteApartmentModelDTOConverter;
        }

        throw new UnsupportedOperationException("Unknown apartment type: " + in.getType());
    }

    @Override
    public ApartmentModelDTOConverter<S> getSourceConverter(ApartmentDTO in) {
        throw new UnsupportedOperationException();
    }
}
