package bynull.realty.dao;

import bynull.realty.DbTest;
import bynull.realty.data.business.Feedback;
import bynull.realty.data.business.User;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class FeedbackRepositoryTest extends DbTest {
    @Resource
    FeedbackRepository repository;

    @Test
    public void saveAndGetFeedbackWithUser() throws Exception {
        User user = createUser();
        Feedback feedback = new Feedback();
        feedback.setText("hello");
        feedback.setCreator(user);
        feedback = repository.saveAndFlush(feedback);

        flushAndClear();

        Feedback found = repository.findOne(feedback.getId());
        assertThat(found, is(notNullValue()));
        assertThat(found.getCreated(), is(notNullValue()));
        assertThat(found.getUpdated(), is(notNullValue()));
        assertThat(found.getText(), is("hello"));
        assertThat(found.getCreator(), is(user));
    }

    @Test
    public void saveAndGetFeedbackWithoutUser() throws Exception {
        Feedback feedback = new Feedback();
        feedback.setText("hello");
        feedback = repository.saveAndFlush(feedback);

        flushAndClear();

        Feedback found = repository.findOne(feedback.getId());
        assertThat(found, is(notNullValue()));
        assertThat(found.getCreated(), is(notNullValue()));
        assertThat(found.getUpdated(), is(notNullValue()));
        assertThat(found.getText(), is("hello"));
        assertThat(found.getCreator(), is(nullValue()));
    }
}