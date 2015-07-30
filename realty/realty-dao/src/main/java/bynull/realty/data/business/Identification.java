package bynull.realty.data.business;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Таблица содержащая все возможные типы идентификаторов по которым можно идентифицировать кого-либо.
 * На эту таблицу должны ссылаться как миниум таблицы apartments and blacklist.
 * <p>
 * Created by null on 7/26/15.
 */
@Setter
@Getter
@Entity
@Table(name = "identification")
public class Identification {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "identification_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "identification_id_generator", sequenceName = "identification_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "facebook_id")
    private Long facebookId;

    @Column(name = "vkontakte_id")
    private Long vkontakteId;

    @Column(name = "phone_number")
    private Long phoneNumber;

    @Column(name = "email")
    private String email;

}
