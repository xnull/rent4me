package bynull.realty.services.api;

/**
* Created by dionis on 04/02/15.
*/
public enum RoomCount {
    ONE("1"), TWO("2"), THREE("3"), FOUR_PLUS("4+");

    public final String value;

    RoomCount(String value) {
        this.value = value;
    }

    public static RoomCount findByValueOrFail(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        for (RoomCount roomCount : values()) {
            if (roomCount.value.equals(value)) return roomCount;
        }
        throw new IllegalArgumentException();
    }
}
