package bynull.realty.web.rest;

import bynull.realty.dao.ApartmentRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.utils.HibernateUtil;
import bynull.realty.web.converters.ApartmentDtoJsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionOperations;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author dionis on 22/06/14.
 */
@Slf4j
@Component
@Path("pre_render")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PreRenderRestResource {
    private final Map<String, String> templateCache = new HashMap<>();

    @Resource
    ApartmentRepository apartmentRepository;

    @Resource
    ApartmentDtoJsonConverter apartmentDtoJsonConverter;

    @Resource
    TransactionOperations transactionOperations;
    public static final int ITEMS_PER_PAGE = 100;


    @GET
    @Path("/sitemap.xml")
    public Response sitemapXml(@PathParam("id") long id) {
        return transactionOperations.execute(tx -> {
            {

                log.info("Rendering sitemap.xml for google bot");
                String template = loadTemplate("sitemap.xml");

                Date date = new DateTime().withYear(2015).withMonthOfYear(4).withDayOfMonth(27).withMillisOfDay(0).toDate();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateFormatted = simpleDateFormat.format(date);

                StringBuilder contentBuilder = new StringBuilder();

                long totalCount = apartmentRepository.countOfActiveApartments();

                long totalCountOfPages = totalCount / ITEMS_PER_PAGE + (totalCount % ITEMS_PER_PAGE > 0 ? 1 : 0);

                for(long page = 1; page <= totalCountOfPages; page++) {
                    String str = urlBuilder(page, simpleDateFormat, date);
                    contentBuilder.append(str);
                }

                String content = contentBuilder.toString();

                template = StringUtils.replace(template, ":urls", content);
                template = StringUtils.replace(template, ":date", dateFormatted);
                return Response.ok(template)
                        .header("content-type", "text/xml; charset=utf-8")
                        .build();

            }
        });
    }

    private String urlBuilder(long page, SimpleDateFormat simpleDateFormat, Date date) {
        Date maxDateForPage = findMaxDateForPage(page);
        Date targetDate = maxDateForPage.after(date) ? maxDateForPage : date;
        String dateFormatted = simpleDateFormat.format(targetDate);

        return "<url>\n" +
                "        <loc>http://rent4.me/search/?page=" + page + "</loc>\n" +
                "        <lastmod>"+dateFormatted+"</lastmod>\n" +
                "    </url>\n";
    }

    @GET
    @Path("/advert/{id}")
    public Response findOne(@PathParam("id") long id) {
        return transactionOperations.execute(tx -> {
            Apartment apartment = apartmentRepository.findOne(id);
            if (apartment == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            } else {
                apartment = HibernateUtil.deproxy(apartment);
                log.info("Rendering content for google bot");
                String template = loadTemplate("apartment.html");
                String desc = getDesc(apartment);
                template = StringUtils.replace(template, ":title", desc);

                String content = "<h1>"+desc+"</h1>";

                if(apartment instanceof SocialNetApartment) {
                    SocialNetApartment netApartment = (SocialNetApartment) apartment;
                    Set<ApartmentExternalPhoto> externalPhotos = netApartment.getExternalPhotos();
                    if(!externalPhotos.isEmpty()) {
                        content += "<p>";
                        for (ApartmentExternalPhoto externalPhoto : externalPhotos) {
                            content += "<img src=\"" + externalPhoto.getImageUrl() + "\"/></br/>";
                        }
                        content += "</p>";
                    }
                } else if(apartment instanceof InternalApartment) {
                    InternalApartment intApartment = (InternalApartment) apartment;
                    List<ApartmentPhoto> externalPhotos = intApartment.listPhotosNewestFirst();
                    if(!externalPhotos.isEmpty()) {
                        content += "<p>";
                        for (ApartmentPhoto externalPhoto : externalPhotos) {
                            content += "<img src=\"" + externalPhoto.getOriginalImageUrl() + "\"/></br/>";
                        }
                        content += "</p>";
                    }
                }

                content+="<p>"+apartment.getDescription()+"</p>";

                content+="<h2>Объявления которые могут быть вам интересны</h2>";
                GeoPoint location = apartment.getLocation();
                if (location != null) {
                    List<Apartment> nearest = apartmentRepository.findNearest(location.getLongitude(), location.getLatitude(), 10, 0);
                    for (Apartment apartment1 : nearest) {
                        if(apartment1.getId().equals(apartment.getId())) continue;

                        content+=("<p>");
                        content+=("<div>");
                        content += ("<a href=\"http://rent4.me/advert/") + (apartment.getId()) + ("\">");
                        content+=(getDesc(apartment1));
                        content+=("</a>");
                        content+=("</div>");
                        content+=("<br/>");


                        content+=("</p>");
                    }
                }

                template = StringUtils.replace(template, ":body", content);
                return Response.ok(template)
                        .header("content-type", "text/html")
                        .build();

            }
        });
    }

    private String loadTemplate(String templateName) {
        String template;
        synchronized (templateCache) {
            if(!templateCache.containsKey(templateName)) {
                InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(templateName);
                try {
                    String join = StringUtils.join(IOUtils.readLines(resourceAsStream), "\n");
                    template = join;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    IOUtils.closeQuietly(resourceAsStream);
                }
            } else {
                template = templateCache.get(templateName);
            }
        }
        return template;
    }

    @Path("/search")
    @GET
    public Response findPosts(
            @QueryParam("page") Integer _page
    ) {

        return transactionOperations.execute(tx -> {
            int page = 1;

            if (_page != null) {
                page = _page;
            }

            List<Apartment> found = loadApartmentsForPage(page);

            log.info("Rendering content for google bot");
            String template = loadTemplate("apartments.html");

            StringBuilder body = new StringBuilder();

            body.append("<p>");

            for (Apartment apartment : found) {
                body.append("<div>");
                body.append("<a href=\"http://rent4.me/advert/").append(apartment.getId()).append("\">");
                body.append(getDesc(apartment));
                body.append("</a>");
                body.append("</div>");
                body.append("<br/>");
            }

            body.append("</p>");

            if (found.size() == ITEMS_PER_PAGE) {
                body.append("<p>");
                body.append("<a href=\"http://rent4.me/search?page=").append(page + 1).append("\">");
                body.append("Вперед");
                body.append("</a>");
                body.append("</p>");
            }

            template = StringUtils.replace(template, ":body", body.toString());
            return Response.ok(template)
                    .header("content-type", "text/html")
                    .build();
        });
    }

    private List<Apartment> loadApartmentsForPage(long page) {
        long offset = (page - 1) * ITEMS_PER_PAGE;

        return apartmentRepository.listApartments(ITEMS_PER_PAGE, offset);
    }

    private Date findMaxDateForPage(long page) {
        long offset = (page - 1) * ITEMS_PER_PAGE;

        return apartmentRepository.findMaxDateForPage(ITEMS_PER_PAGE, offset);
    }

    private static String getDesc(Apartment apartment) {
        apartment = HibernateUtil.deproxy(apartment);
        String result = "Аренда ";
        switch (apartment.getTarget()) {
            case RENTER:
                result+="Сдам ";
                break;
            case LESSOR:
                result+="Cниму ";
                break;
            default:
                result+="Сдам/Cниму ";

        }

        Integer roomCount = apartment.getRoomCount();
        if (roomCount != null) {
            if(roomCount == 1) {
                result+="1 комнатную ";
            } else if(roomCount == 2) {
                result+="2-ух комнатную ";
            } else if(roomCount == 3) {
                result+="3-ех комнатную ";
            } else if(roomCount == 4) {
                result+="4-ех комнатную ";
            }
        }

        result += "квартиру ";

        if(apartment.getAddressComponents() != null && apartment.getAddressComponents().getCity() != null) {
            result += "в "+(apartment.getAddressComponents().getCity().toLowerCase().contains("город")?"":"городе ")+apartment.getAddressComponents().getCity()+" ";
            if (apartment.getAddressComponents().getDistrict() != null) {
                result += apartment.getAddressComponents().getDistrict()+" ";
            }
        } else {
            switch (apartment.getDataSource()) {
                case VKONTAKTE:
                    VkontakteApartment apartment1 = (VkontakteApartment) apartment;
                    CityEntity city = apartment1.getVkontaktePage().getCity();
                    if (city != null) {
                        result += "в "+(city.getName().toLowerCase().contains("город")?"":"городе ")+city.getName()+" ";
                    }
                    break;
                case FACEBOOK:
                    FacebookApartment apartment2 = (FacebookApartment) apartment;
                    CityEntity city2 = apartment2.getFacebookPage().getCity();
                    if (city2 != null) {
                        result += "в "+(city2.getName().toLowerCase().contains("город")?"":"городе ")+city2.getName()+" ";
                    }
                    break;
            }
        }


        if(apartment.getRentalFee() != null) {
            result += apartment.getRentalFee()+" рублей в месяц ";
        }

        return result.trim();
    }
}
