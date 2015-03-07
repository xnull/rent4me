package bynull.realty.data.business;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    @Email
    @Column(name = "email")
    private String email;
    @Embedded
    private PhoneNumber phoneNumber;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "age")
    private Integer age;
    @Column(name = "facebook_id")
    private String facebookId;
    @Column(name = "vkontakte_id")
    private String vkontakteId;
    @Column(name = "fb_access_token")
    private String fbAccessToken;
    @Column(name = "fb_access_token_expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fbAccessTokenExpiration;
    @Column(name = "vk_access_token")
    private String vkAccessToken;
    @Column(name = "vk_access_token_expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vkAccessTokenExpiration;

    @JoinTable(name = "realty_users_authorities", joinColumns = {
            @JoinColumn(name = "user_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "authority_id")
    })
    @OneToMany(fetch = FetchType.EAGER, cascade = javax.persistence.CascadeType.ALL)
    private Set<Authority> authorities;
    /**
     * Set of my apartments
     */
    @JoinColumn(name = "owner_id")
    @OneToMany
    private Set<InternalApartment> apartments;

    /**
     * Set of rental histories - for users who rented history(not owners).
     */
    @JoinTable(name = "rental_histories_vw",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "rental_history_id")
            }
    )
    @OneToMany
    private Set<RentalHistory> rentalHistories;

    @Override
    public Collection<Authority> getAuthorities() {
        return Collections.unmodifiableSet(authorities);
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public void addAuthority(Authority authority) {
        Assert.notNull(authority);
        if (authorities == null) {
            authorities = new HashSet<Authority>();
        }
        Iterator<Authority> iterator = authorities.iterator();
        boolean hasAuthority = false;
        while (iterator.hasNext()) {
            Authority next = iterator.next();
            if (next.getName() == authority.getName()) {
                hasAuthority = true;
                break;
            }
        }
        if (!hasAuthority) {
            authorities.add(authority);
        }
    }

    public void removeAuthority(Authority authority) {
        Assert.notNull(authority);
        if (authorities == null) return;
        Iterator<Authority> iterator = authorities.iterator();
        while (iterator.hasNext()) {
            Authority next = iterator.next();
            if (next.getName() == authority.getName()) iterator.remove();
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

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<InternalApartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<InternalApartment> apartments) {
        this.apartments = apartments;
    }

    public Set<RentalHistory> getRentalHistories() {
        return rentalHistories;
    }

    public void setRentalHistories(Set<RentalHistory> rentalHistories) {
        this.rentalHistories = rentalHistories;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getVkontakteId() {
        return vkontakteId;
    }

    public void setVkontakteId(String vkontakteId) {
        this.vkontakteId = vkontakteId;
    }

    public String getFbAccessToken() {
        return fbAccessToken;
    }

    public void setFbAccessToken(String fbAccessToken) {
        this.fbAccessToken = fbAccessToken;
    }

    public Date getFbAccessTokenExpiration() {
        return fbAccessTokenExpiration;
    }

    public void setFbAccessTokenExpiration(Date fbAccessTokenExpiration) {
        this.fbAccessTokenExpiration = fbAccessTokenExpiration;
    }

    public String getVkAccessToken() {
        return vkAccessToken;
    }

    public void setVkAccessToken(String vkAccessToken) {
        this.vkAccessToken = vkAccessToken;
    }

    public Date getVkAccessTokenExpiration() {
        return vkAccessTokenExpiration;
    }

    public void setVkAccessTokenExpiration(Date vkAccessTokenExpiration) {
        this.vkAccessTokenExpiration = vkAccessTokenExpiration;
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
