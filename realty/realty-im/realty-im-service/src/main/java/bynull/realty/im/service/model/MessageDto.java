package bynull.realty.im.service.model;

import bynull.realty.im.service.model.ids.MessageId;
import lombok.Value;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@Value
public class MessageDto {
    private final MessageId messageId;
    private final String text;
}
