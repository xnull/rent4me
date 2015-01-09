package bynull.realty.data.business.chat;

import bynull.realty.data.business.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 06/01/15.
 */
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "chat_msg_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "chat_msg_id_generator", sequenceName = "chat_msgs_id_seq", allocationSize = 1)
    private Long id;
    @NotNull
    @NotEmpty
    @Length(max = 8196)
    @Column(name = "message")
    private String message;
    @JoinColumn(name = "sender_id")
    @ManyToOne(optional = false)
    private User sender;
    @JoinColumn(name = "receiver_id")
    @ManyToOne(optional = false)
    private User receiver;
    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    /**
     * Chat key that uniquely identifies chat and participants in it.
     */
    @Embedded
    private ChatKey chatKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Date getCreated() {
        return copy(created);
    }

    void setCreated(Date created) {
        this.created = copy(created);
    }

    public ChatKey getChatKey() {
        return ChatKey.copy(chatKey);
    }

    void setChatKey(ChatKey chatKey) {
        this.chatKey = ChatKey.copy(chatKey);
    }

    @PrePersist
    void prePersist() {
        setCreated(new Date());
        User sender = getSender();
        User receiver = getReceiver();
        ChatKey chatKey = ChatKeyGenerator.getChatKey(sender, receiver);
        setChatKey(chatKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatMessage)) return false;

        ChatMessage that = (ChatMessage) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Embeddable
    @Getter
    @AllArgsConstructor
    public static class ChatKey implements Serializable {
        @Column(name = "chat_key")
        private String key;

        public static ChatKey copy(ChatKey key) {
            if (key == null) {
                return null;
            } else {
                return new ChatKey(key.getKey());
            }
        }

        public ChatKey() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ChatKey)) return false;

            ChatKey chatKey = (ChatKey) o;

            if (getKey() != null ? !getKey().equals(chatKey.getKey()) : chatKey.getKey() != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return getKey() != null ? getKey().hashCode() : 0;
        }
    }

    public static abstract class ChatKeyGenerator {
        private ChatKeyGenerator() {
            throw new UnsupportedOperationException("Instantiation not supported");
        }

        public static ChatKey getChatKey(User user1, User user2) {
            Long id1 = user1.getId();
            Assert.notNull(id1);
            Long id2 = user2.getId();
            Assert.notNull(id2);
            Assert.isTrue(!id1.equals(id2));

            long minId = Math.min(id1, id2);
            long maxId = Math.max(id1, id2);
            String key = minId + "_" + maxId;
            return new ChatKey(key);
        }
    }
}
