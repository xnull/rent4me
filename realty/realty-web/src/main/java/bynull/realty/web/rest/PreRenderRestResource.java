package bynull.realty.web.rest;

import bynull.realty.dao.apartment.ApartmentRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.common.CityEntity;
import bynull.realty.utils.HibernateUtil;
import bynull.realty.web.converters.ApartmentDtoJsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
    public static final int CUSTOM_URLS_COUNT = 2;
    private final Map<String, String> templateCache = new HashMap<>();

    @Resource
    ApartmentRepository apartmentRepository;

    @Resource
    ApartmentDtoJsonConverter apartmentDtoJsonConverter;

    @Resource
    TransactionOperations transactionOperations;
    public static final int ITEMS_PER_PAGE = 50000;


    @GET
    @Path("/sitemap-index.xml")
    public Response sitemapIndexXml() {
        long totalCount = CUSTOM_URLS_COUNT + apartmentRepository.countOfActiveApartments();

        long totalCountOfPages = totalCount / ITEMS_PER_PAGE + (totalCount % ITEMS_PER_PAGE > 0 ? 1 : 0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


        String result = "";

        for(int page=1; page <= totalCountOfPages; page++) {
            Date maxDateForPage = findMaxDateForPage(page);
            String dateFormatted = simpleDateFormat.format(maxDateForPage);
            result += "<sitemap>\n" +
                    "        <loc>http://rent4.me/sitemap-"+page+".xml</loc>\n" +
                    "        <lastmod>"+dateFormatted+"</lastmod>\n" +
                    "    </sitemap>";
        }

        String template = loadTemplate("sitemap-index.xml");

        template = StringUtils.replace(template, ":sitemaps", result);

        return Response.ok(template)
                .header("content-type", "text/xml; charset=utf-8").build();
    }
    @GET
    @Path("/sitemap.xml")
    public Response sitemapXml(@PathParam("id") long _pageId) {
        final long pageId = _pageId < 1 ? 1 : _pageId;
        return transactionOperations.execute(tx -> {
            {

                log.info("Rendering sitemap.xml for google bot");
                String template = pageId == 1 ? loadTemplate("sitemap.xml") : loadTemplate("sitemap-nth.xml");

                Date date = new DateTime().withYear(2015).withMonthOfYear(4).withDayOfMonth(27).withMillisOfDay(0).toDate();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateFormatted = simpleDateFormat.format(date);

                StringBuilder contentBuilder = new StringBuilder();

                long totalCount = CUSTOM_URLS_COUNT + apartmentRepository.countOfActiveApartments();

                long totalCountOfPages = totalCount / ITEMS_PER_PAGE + (totalCount % ITEMS_PER_PAGE > 0 ? 1 : 0);

                List<Apartment> found = loadApartmentsForPage(pageId);
                for (Apartment apartment : found) {
                    String str = urlBuilder(apartment, pageId, simpleDateFormat);
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

    private String urlBuilder(Apartment apartment, long page, SimpleDateFormat simpleDateFormat) {
        String dateFormatted = simpleDateFormat.format(apartment.getCreated());

        return "<url>\n" +
                "        <loc>" + urlForApartment(apartment) + "</loc>\n" +
                "        <lastmod>"+dateFormatted+"</lastmod>\n" +
                "    </url>\n";
    }

    @GET
    @Path("/sitemap-{id}.xml")
    public Response sitemapNthXml(@PathParam("id") long id) {
        return sitemapXml(id);
    }

    private String searchUrlBuilder(long page, SimpleDateFormat simpleDateFormat, Date date) {
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
                template = StringUtils.replace(template, ":desc", desc);

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

                List<Apartment> similar = apartmentRepository.findSimilarApartments(apartment.getId());
                for (Apartment suggestedApartment : similar) {
                    if(suggestedApartment.getId().equals(apartment.getId())) continue;

                    content+=("<p>");
                    content+=("<div>");
                    content += "<a href=\"" + urlForApartment(suggestedApartment) + ("\">");
                    content+=(getDesc(suggestedApartment));
                    content+=("</a>");
                    content+=("</div>");
                    content+=("<br/>");


                    content+=("</p>");
                }

                template = StringUtils.replace(template, ":body", content);
                return Response.ok(template)
                        .header("content-type", "text/html")
                        .build();

            }
        });
    }

    private String urlForApartment(Apartment apartment) {
        return "http://rent4.me/advert/" + (apartment.getId());
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

        int itemsToLoad = itemsToLaod(page);

        return apartmentRepository.listApartments(itemsToLoad, offset);
    }

    private int itemsToLaod(long page) {
        int itemsToLoad = ITEMS_PER_PAGE;

        if(page == 1) {
            itemsToLoad -= CUSTOM_URLS_COUNT;
        }
        return itemsToLoad;
    }

    private Date findMaxDateForPage(long page) {
        long offset = (page - 1) * ITEMS_PER_PAGE;

        int itemsToLoad = itemsToLaod(page);

        return apartmentRepository.findMaxDateForPage(itemsToLoad, offset);
    }

    private static String getDesc(Apartment apartment) {
        apartment = HibernateUtil.deproxy(apartment);
        String result = "";
        final String apartmentSizeSuffix;
        final String apartmentSuffix;
        switch (apartment.getTarget()) {
            case RENTER:
                result+="Снять ";
                apartmentSizeSuffix = "ую";
                apartmentSuffix = "у";
                break;
            case LESSOR:
                result+="Аренда ";
                apartmentSizeSuffix = "ой";
                apartmentSuffix = "ы";
                break;
            default:
                result+="Снять ";
                apartmentSizeSuffix = "ую";
                apartmentSuffix = "у";

        }

        Integer roomCount = apartment.getRoomCount();
        if (roomCount != null) {
            if(roomCount == 1) {
                result+="одно комнатн"+apartmentSizeSuffix+" ";
            } else if(roomCount == 2) {
                result+="двух комнатн"+apartmentSizeSuffix+" ";
            } else if(roomCount == 3) {
                result+="трех комнатн"+apartmentSizeSuffix+" ";
            } else if(roomCount == 4) {
                result+="четырех комнатн"+apartmentSizeSuffix+" ";
            }
        }

        result += "квартир"+apartmentSuffix+" ";

        if(apartment.getAddressComponents() != null && apartment.getAddressComponents().getCity() != null) {
            result += normalizeName(apartment.getAddressComponents().getCity());
            if (apartment.getAddressComponents().getDistrict() != null) {
                result += apartment.getAddressComponents().getDistrict()+" ";
            }
        } else {
            switch (apartment.getDataSource()) {
                case VKONTAKTE:
                    VkontakteApartment apartment1 = (VkontakteApartment) apartment;
                    CityEntity city = apartment1.getVkontaktePage().getCity();
                    if (city != null) {
                        result += normalizeName(city.getName());
                    }
                    break;
                case FACEBOOK:
                    FacebookApartment apartment2 = (FacebookApartment) apartment;
                    CityEntity city2 = apartment2.getFacebookPage().getCity();
                    if (city2 != null) {
                        result += normalizeName(city2.getName());
                    }
                    break;
            }
        }


        if(apartment.getRentalFee() != null) {
            result += apartment.getRentalFee()+" рублей в месяц ";
        }

        //perform inverse actions
        /*switch (apartment.getTarget()) {
            case RENTER:
                result+="[сниму квартиру] ";
                break;
            case LESSOR:
                result+="[сдам квартиру] ";
                break;
            default:;

        }*/

        return result.trim();
    }

    private static String normalizeName(String name) {
        String result = StringUtils.trimToEmpty(name);
        result = result.endsWith("а") ? result.substring(0, result.length() - 1)+"е" : result;
        return "в "+result+ " ";
    }
}
