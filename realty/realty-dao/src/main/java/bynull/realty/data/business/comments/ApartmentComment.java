package bynull.realty.data.business.comments;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.User;

import javax.persistence.*;

/**
 * @author dionis on 25/06/14.
 */
@Entity
@Table(name = "apartment_comments")
@SequenceGenerator(name = "comment_id_generator", sequenceName = "apartment_comment_id_seq", allocationSize = 1)
public class ApartmentComment extends Comment {
    @JoinColumn(name = "commented_apartment_id")
    @ManyToOne
    private Apartment commentedApartment;
}
