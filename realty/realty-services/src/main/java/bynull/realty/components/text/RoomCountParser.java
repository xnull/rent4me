package bynull.realty.components.text;

import bynull.realty.utils.TextUtils;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by dionis on 20/01/15.
 */
@Slf4j
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
                Pattern.compile("((.*)(одн([^\\s\\.,]){1,3})(.{0,5})(комнатн([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(одн([^\\s\\.,]){1,5})(.{0,5})(к(([^\\s\\.,]){0,8})(.{0,2})(кв([^\\s\\.,])*))(.*))", FLAGS),
                Pattern.compile("((.*)(1(\\-)?([^\\s\\.,]){1,3})(.{0,5})(к(([^\\s\\.,]){0,8})(.{0,2})(кв([^\\s\\.,])*))(.*))", FLAGS),
                Pattern.compile("((.*)(1(\\-)?([^\\s\\.,]){1,3})(.{0,5})(комнатн([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(1(\\-)?([^\\s\\.,]){1,3})(.{0,5})(ушк([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(1((\\-)?)((.)?)к([^\\s\\.,]){0,5})(.{0,10})(кв([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(1((\\-)?)((.){0,5})комн([^\\s\\.,]){0,5})(.{0,10})(кв([^\\s\\.,])*)(.*))", FLAGS)
        );

        twoRoomApartmentStems = ImmutableSet.of(
                porter.stem("двукомнатная"),
                porter.stem("двухкомнатная"),
                porter.stem("двушка"),
                porter.stem("двойная")
        );

        twoRoomApartmentPatterns = ImmutableSet.of(
                Pattern.compile("((.*)(дв([^\\s\\.,]){1,5})(.{0,5})(комнат([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(дв([^\\s\\.,]){0,5})(.{0,5})(к(([^\\s\\.,]){0,8})(.{0,2})(кв([^\\s\\.,])*))(.*))", FLAGS),
                Pattern.compile("((.*)(2(\\-)?(([^\\s\\.,]){0,3}))(.{0,5})(комнатн([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(2(\\-)?([^\\s\\.,]){0,2}х)(.{0,5})(к(([^\\s\\.,]){0,8})(.{0,2})(кв([^\\s\\.,])*))(.*))", FLAGS),
                Pattern.compile("((.*)(2(\\-)?([^\\s\\.,]){0,2}х)(.{0,5})(ушк([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(2((\\-)?([^\\s\\.,]){0,3})((.)?)к([^\\s\\.,]){0,5})(.{0,10})(кв([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(2((\\-)?([^\\s\\.,]){0,3})((.)?)к(а|у)([^\\s\\.,]){0,5})(.*))", FLAGS),
                Pattern.compile("((.*)(2((\\-)?([^\\s\\.,]){0,2}х)((.){0,5})комн([^\\s\\.,]){0,5})(.{0,10})(кв([^\\s\\.,])*)(.*))", FLAGS)
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
                Pattern.compile("((.*)(тр(е|ё)х([^\\s\\.,]){0,5})(.{0,5})(комнат([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(тр(е|ё)([^\\s\\.,]){0,5})(.{0,5})(комнат([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(тр(е|ё)([^\\s\\.,]){0,5})(.{0,5})(к(([^\\s\\.,]){0,8})(.{0,2})(кв([^\\s\\.,])*))(.*))", FLAGS),
                Pattern.compile("((.*)(три([^\\s\\.,]){0,5})(.{0,5})(комнат([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(тр(е|ё)шк([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(3(\\-)?([^\\s\\.,]){0,5})(.?)(к(([^\\s\\.,]){0,8})(.{0,2})(кв([^\\s\\.,])*))(.*))", FLAGS),
                Pattern.compile("((.*)(3(\\-)?([^\\s\\.,]){0,5})(.{0,5})(комнатн([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(3(\\-)?([^\\s\\.,]){0,5})(.{0,5})(ушк([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(3(\\-)?([^\\s\\.,]){0,5})((.)?)(((е|ё)?)шк([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(3((\\-)?)((.){0,5})к([^\\s\\.,]){0,5})(.{0,10})(кв([^\\s\\.,])*)(.*))", FLAGS),
                Pattern.compile("((.*)(3((\\-)?)((.){0,5})комн([^\\s\\.,]){0,5})(.{0,10})(кв([^\\s\\.,])*)(.*))", FLAGS)
        );

    }

    public static RoomCountParser getInstance() {
        return INSTANCE;
    }

    public Integer findRoomCount(String text) {
        text = StringUtils.trimToEmpty(text).toLowerCase();
        if (text.isEmpty()) return null;

        text = TextUtils.normalizeTextForParsing(text);

        // кейсы 3-ех комнатных квартир
        for (String stem : threeRoomApartmentStems) {
            if (text.contains(stem)) {
                log.debug("3-room Matched by stem [{}]", stem);
                return 3;
            }
        }
        for (Pattern pattern : threeRoomApartmentPatterns) {
            if (pattern.matcher(text).matches()) {
                log.debug("3-room Matched by pattern [{}]", pattern.pattern());
                return 3;
            }
        }

        // кейсы 2-ух комнатной квартиры
        for (String stem : twoRoomApartmentStems) {
            if (text.contains(stem)) {
                log.debug("2-room Matched by stem [{}]", stem);
                return 2;
            }
        }
        for (Pattern pattern : twoRoomApartmentPatterns) {
            if (pattern.matcher(text).matches()) {
                log.debug("2-room Matched by pattern [{}]", pattern.pattern());
                return 2;
            }
        }

        // кейсы однокомнатной квартиры
        for (String stem : oneRoomApartmentStems) {
            if (text.contains(stem)) {
                log.debug("1-room Matched by stem [{}]", stem);
                return 1;
            }
        }
        for (Pattern pattern : oneRoomApartmentPatterns) {
            if (pattern.matcher(text).matches()) {
                log.debug("1-room Matched by pattern [{}]", pattern.pattern());
                return 1;
            }
        }

        return null;
    }
}
