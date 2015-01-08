package bynull.realty.dto.vk;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by trierra on 12/20/14.
 */
@Getter
@Setter
public class ItemDTO extends BaseEntity {

    String formId;
    String ownerId;
    Date date;
    List<AttachmentDTO> attachmentDTOs;

    public ItemDTO() {

    }

    public static ItemDTO from() {
//        if (model == null) return null;
        ItemDTO dto = new ItemDTO();
        //TODO: to complete
        return dto;
    }
}
