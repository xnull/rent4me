package bynull.realty.data.business;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @author dionis on 23/06/14.
 */
@Entity
    @Table(name = "realty_authorities")
public class Authority implements GrantedAuthority {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "authority_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "authority_id_generator", sequenceName = "realty_authorities_id_seq", allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Name name;

    /**
     * Default constructor needed for hibernate
     */
    public Authority() {
    }

    public Authority(Name name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    @Override
    public String getAuthority() {
        return getName().name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authority)) return false;

        Authority authority = (Authority) o;

        if (getId() != null ? !getId().equals(authority.getId()) : authority.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    public static enum Name {
        ROLE_USER, ROLE_ADMIN
    }
}
