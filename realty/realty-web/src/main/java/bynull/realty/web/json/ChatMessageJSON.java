package bynull.realty.web.json;

import bynull.realty.dao.util.Constants;
import bynull.realty.dto.ChatMessageDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by dionis on 06/01/15.
 */
@Getter
@Setter
public class ChatMessageJSON {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("message")
    private String message;
    @JsonProperty("sender")
    private UserJSON sender;
    @JsonProperty("receiver")
    private UserJSON receiver;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
    @JsonProperty("created")
    private Date created;
    @JsonProperty("chat_key")
    private String chatKey;

    public static ChatMessageJSON from(ChatMessageDTO dto) {
        if (dto == null) {
            return null;
        }
        ChatMessageJSON json = new ChatMessageJSON();
        json.setId(dto.getId());
        json.setMessage(dto.getMessage());
        json.setSender(UserJSON.from(dto.getSender()));
        json.setReceiver(UserJSON.from(dto.getReceiver()));
        json.setCreated(dto.getCreated());
        json.setChatKey(dto.getChatKey() != null ? dto.getChatKey().getKey() : null);
        return json;
    }
}
