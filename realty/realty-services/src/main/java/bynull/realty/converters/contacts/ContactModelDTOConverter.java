package bynull.realty.converters.contacts;

import bynull.realty.common.Converter;
import bynull.realty.data.business.Contact;
import bynull.realty.dto.ContactDTO;

/**
 * Created by dionis on 3/5/15.
 */
public interface ContactModelDTOConverter<ST extends Contact> extends Converter<ST, ContactDTO> {
}
