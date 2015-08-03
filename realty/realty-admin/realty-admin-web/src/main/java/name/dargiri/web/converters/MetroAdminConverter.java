package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.MetroDTO;
import name.dargiri.web.form.GeoPointForm;
import name.dargiri.web.form.MetroForm;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class MetroAdminConverter implements Converter<MetroDTO, MetroForm> {

    @Override
    public MetroForm newTargetType(Optional<MetroDTO> in) {
        return new MetroForm();
    }

    @Override
    public MetroDTO newSourceType(MetroForm in) {
        return new MetroDTO();
    }

    @Override
    public Optional<MetroForm> toTargetType(Optional<MetroDTO> in, MetroForm form) {
        return in.map(m -> {
            form.setId(m.getId());
            form.setStationName(m.getStationName());
            form.setLocation(GeoPointForm.from(m.getLocation()));
            return form;
        });
    }

    @Override
    public MetroDTO toSourceType(MetroForm in, MetroDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setStationName(in.getStationName());
        dto.setLocation(GeoPointForm.toDTO(in.getLocation()));
        return dto;
    }
}
