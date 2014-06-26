package bynull.realty.data.business.comments;

import bynull.realty.data.business.User;

import javax.persistence.*;

/**
 * @author dionis on 25/06/14.
 */
@Entity
@Table(name = "user_comments")
@SequenceGenerator(name = "comment_id_generator", sequenceName = "user_comment_id_seq", allocationSize = 1)
public class UserComment extends Comment {
    @JoinColumn(name = "commented_user_id")
    @ManyToOne
    private User commentedUser;
}
