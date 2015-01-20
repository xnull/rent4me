package bynull.realty.components.text;

import bynull.realty.utils.TextUtils;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by dionis on 20/01/15.
 */
public class RoomCountParser {

    public static final int FLAGS = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE;
    private static final RoomCountParser INSTANCE = new RoomCountParser();
    private final Porter porter = Porter.getInstance();

    private final Set<String> oneRoomApartmentStems;
    private final Set<Pattern> oneRoomApartmentPatterns;

    private final Set<String> twoRoomApartmentStems;
    private final Set<Pattern> twoRoomApartmentPatterns;

    private final Set<String> threeRoomApartmentStems;
    private final Set<Pattern> threeRoomApartmentPatterns;

    private RoomCountParser() {
        oneRoomApartmentStems = ImmutableSet.of(
                porter.stem("однокомнатная"),
                porter.stem("однушка"),
                porter.stem("одинарная")
        );

        oneRoomApartmentPatterns = ImmutableSet.of(
                Pattern.compile("((.*)(одн\\S{1,3})(.{0,5})(комнатн\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(1(\\-)?\\S{1,3})(.{0,5})(комнатн\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(1(\\-)?\\S{1,3})(.{0,5})(ушк\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(1((\\-)?)((.){0,5})к\\S{0,5})(.{0,10})(кв\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(1((\\-)?)((.){0,5})комн\\S{0,5})(.{0,10})(кв\\S*)(.*))", FLAGS)
        );

        twoRoomApartmentStems = ImmutableSet.of(
                porter.stem("двукомнатная"),
                porter.stem("двухкомнатная"),
                porter.stem("двушка"),
                porter.stem("двойная")
        );

        twoRoomApartmentPatterns = ImmutableSet.of(
                Pattern.compile("((.*)(дв\\S{1,5})(.{0,5})(комнат\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(2(\\-)?\\S{1,5})(.{0,5})(комнатн\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(2(\\-)?\\S{1,5})(.{0,5})(ушк\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(2((\\-)?)((.){0,5})к\\S{0,5})(.{0,10})(кв\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(2((\\-)?)((.){0,5})к(а|у)\\S{0,5})(.*))", FLAGS),
                Pattern.compile("((.*)(2((\\-)?)((.){0,5})комн\\S{0,5})(.{0,10})(кв\\S*)(.*))", FLAGS)
        );

        threeRoomApartmentStems = ImmutableSet.of(
                porter.stem("трехкомнатная"),
                porter.stem("трикомнатная"),
                porter.stem("трекомнатная"),
                porter.stem("трушка"),
                porter.stem("трешка"),
                porter.stem("троиная")
        );

        threeRoomApartmentPatterns = ImmutableSet.of(
                Pattern.compile("((.*)(тр(е|ё)х\\S{0,5})(.{0,5})(комнат\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(тр(е|ё)\\S{0,5})(.{0,5})(комнат\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(три\\S{0,5})(.{0,5})(комнат\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(тр(е|ё)шк\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(3(\\-)?\\S{0,5})(.{0,5})(комнатн\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(3(\\-)?\\S{0,5})(.{0,5})(ушк\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(3(\\-)?\\S{0,5})(.{0,5})(((е|ё)?)шк\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(3((\\-)?)((.){0,5})к\\S{0,5})(.{0,10})(кв\\S*)(.*))", FLAGS),
                Pattern.compile("((.*)(3((\\-)?)((.){0,5})комн\\S{0,5})(.{0,10})(кв\\S*)(.*))", FLAGS)
        );

    }

    public static RoomCountParser getInstance() {
        return INSTANCE;
    }

    public Integer findRoomCount(String text) {
        text = StringUtils.trimToEmpty(text).toLowerCase()
//                .replace('ё', 'e').replace('й', 'и')
        ;
        if (text.isEmpty()) return null;

        text = TextUtils.normalizeTextForParsing(text);


        // кейсы однокомнатной квартиры
        for (String stem : oneRoomApartmentStems) {
            if (text.contains(stem)) {
                return 1;
            }
        }
        for (Pattern pattern : oneRoomApartmentPatterns) {
            if (pattern.matcher(text).matches()) {
                return 1;
            }
        }

        // кейсы 2-ух комнатной квартиры
        for (String stem : twoRoomApartmentStems) {
            if (text.contains(stem)) {
                return 2;
            }
        }
        for (Pattern pattern : twoRoomApartmentPatterns) {
            if (pattern.matcher(text).matches()) {
                return 2;
            }
        }

        // кейсы 3-ех комнатных квартир
        for (String stem : threeRoomApartmentStems) {
            if (text.contains(stem)) {
                return 3;
            }
        }
        for (Pattern pattern : threeRoomApartmentPatterns) {
            if (pattern.matcher(text).matches()) {
                return 3;
            }
        }

        return null;
    }
}
