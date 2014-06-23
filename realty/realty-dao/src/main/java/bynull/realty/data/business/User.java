package bynull.realty.data.business;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

/**
 * Created by null on 21.06.14.
 */
@Entity
@Table(name = "realty_users")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "user_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_id_generator", sequenceName = "realty_users_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password_hash")
    private String passwordHash;
    @JoinTable(name = "realty_users_authorities", joinColumns = {
            @JoinColumn(name = "user_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "authority_id")
    })

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    @Override
    public Collection<Authority> getAuthorities() {
        return Collections.unmodifiableSet(authorities);
    }

    public void addAuthority(Authority authority) {
        Assert.notNull(authority);
        if(authorities == null) {
            authorities = new HashSet<Authority>();
        }
        Iterator<Authority> iterator = authorities.iterator();
        boolean hasAuthority = false;
        while (iterator.hasNext()) {
            Authority next = iterator.next();
            if(next.getName() == authority.getName()) {
                hasAuthority = true;
                break;
            }
        }
        if(!hasAuthority) {
            authorities.add(authority);
        }
    }

    public void removeAuthority(Authority authority) {
        Assert.notNull(authority);
        if(authorities == null) return;
        Iterator<Authority> iterator = authorities.iterator();
        while (iterator.hasNext()) {
            Authority next = iterator.next();
            if(next.getName() == authority.getName()) iterator.remove();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getId() != null ? !getId().equals(user.getId()) : user.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
