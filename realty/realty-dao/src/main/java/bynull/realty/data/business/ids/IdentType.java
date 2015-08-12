package bynull.realty.data.business.ids;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Типы идентификаторов существующих в системе, по которым можно попытаться идентифицировать пользователя
 * Created by null on 8/2/15.
 */
public enum IdentType {

    USER_ID("USER"), FB_ID("FB"), VK_ID("VK"), PHONE("PHONE"), EMAIL("EMAIL"),
    /**
     * Объявление о сдаче или съёме недвижимости. Оно соотнесено с id таблицы apartments
     */
    APARTMENT("APT");

    private final String type;

    IdentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    private static Map<String, IdentType> index = Arrays
            .stream(IdentType.values())
            .collect(Collectors.toMap(IdentType::getType, t-> t));

    public static IdentType from(String type){
        IdentType t = index.get(type);
        if (t == null){
            throw new IllegalArgumentException("Wrong ident type: " + type);
        }
        return t;
    }
}
