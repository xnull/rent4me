package bynull.realty.data.business.external.facebook;

import bynull.realty.DbTest;
import bynull.realty.dao.MetroRepository;
import bynull.realty.dao.external.FacebookPageToScrapRepository;
import bynull.realty.dao.external.FacebookScrapedPostRepository;
import bynull.realty.dao.geo.CityRepository;
import bynull.realty.dao.geo.CountryRepository;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.CountryEntity;
import bynull.realty.data.common.GeoPoint;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class FacebookScrapedPostTest extends DbTest {
    @Resource
    FacebookScrapedPostRepository repository;

    @Resource
    FacebookPageToScrapRepository facebookPageToScrapRepository;

    @Resource
    MetroRepository metroRepository;
    @Resource
    CityRepository cityRepository;
    @Resource
    CountryRepository countryRepository;

    @Test
    public void metroRelationTest() {
        MetroEntity metro = createMetro();

        FacebookPageToScrap page = createPage();

        FacebookScrapedPost post = new FacebookScrapedPost();
        post.setFacebookPageToScrap(page);
        post.setExternalId(UUID.randomUUID().toString());
        post.setFacebookPageToScrap(page);
        post.setMetros(ImmutableSet.of(metro));

        post = repository.saveAndFlush(post);
        assertThat(post, is(notNullValue()));
        assertThat(Iterables.getFirst(post.getMetros(), null), is(notNullValue()));
    }

    @Test
    public void metroDeletionTestShouldNotDeleteByCascade() {
        MetroEntity metro = createMetro();

        FacebookPageToScrap page = createPage();

        FacebookScrapedPost post = new FacebookScrapedPost();
        post.setFacebookPageToScrap(page);
        post.setExternalId(UUID.randomUUID().toString());
        post.setFacebookPageToScrap(page);
        post.setMetros(ImmutableSet.of(metro));

        post = repository.saveAndFlush(post);
        assertThat(post, is(notNullValue()));
        assertThat(Iterables.getFirst(post.getMetros(), null), is(notNullValue()));

        metroRepository.delete(metro);
        flushAndClear();

        FacebookScrapedPost found = repository.findOne(post.getId());
        assertThat(found, is(notNullValue()));
        assertThat(found.getMetros(), empty());
    }

    private MetroEntity createMetro() {
        CountryEntity country = new CountryEntity();
        country.setName("Russia");
        country = countryRepository.saveAndFlush(country);


        CityEntity moscowCity = new CityEntity();
        moscowCity.setName("Moscow");
        moscowCity.setCountry(country);
        moscowCity = cityRepository.saveAndFlush(moscowCity);

        MetroEntity metro = new MetroEntity();
        metro.setCity(moscowCity);
        metro.setLocation(new GeoPoint(1, 2));
        metro.setStationName("Name");
        metro = metroRepository.saveAndFlush(metro);
        return metro;
    }

    private FacebookPageToScrap createPage() {
        FacebookPageToScrap page = new FacebookPageToScrap();
        page.setExternalId(UUID.randomUUID().toString());
        page = facebookPageToScrapRepository.saveAndFlush(page);
        return page;
    }
}