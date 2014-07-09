package bynull.realty.data.business;

import javax.persistence.*;
import java.util.Date;

/**
 * @author dionis on 09/07/14.
 */
@Entity
@Table(name = "user_tokens")
public class UserToken {
    @Id
    @GeneratedValue(generator = "user_token_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_token_id_generator", sequenceName = "user_tokens_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "user_id")
    @ManyToOne()
    private User user;
    @Column(name = "token")
    private String token;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt")
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt")
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreated() {
        return created;
    }

    void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    void setUpdated(Date updated) {
        this.updated = updated;
    }

    @PrePersist
    void prePersist() {
        Date date = new Date();
        setCreated(date);
        setUpdated(date);
    }

    @PreUpdate
    void preUpdate() {
        setUpdated(new Date());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserToken)) return false;

        UserToken userToken = (UserToken) o;

        if (getId() != null ? !getId().equals(userToken.getId()) : userToken.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
