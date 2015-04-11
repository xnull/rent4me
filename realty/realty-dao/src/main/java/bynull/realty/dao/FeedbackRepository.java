package bynull.realty.dao;

import bynull.realty.data.business.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dionis on 4/11/15.
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
