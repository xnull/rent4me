package bynull.realty.data.business.ids;

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
}
