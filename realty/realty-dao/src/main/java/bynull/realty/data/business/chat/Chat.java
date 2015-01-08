package bynull.realty.data.business.chat;

import bynull.realty.data.business.User;

import javax.persistence.*;

/**
 * Created by dionis on 06/01/15.
 */
//@Entity
@Table(name = "chats_vw")
public class Chat {
    @Id
    @Column(name = "id")
    private String id;
    @JoinColumn(name = "conversation_target_id")
    @ManyToOne
    private User conversationTarget;
    @ManyToOne
    private ChatMessage lastMessage;
}
