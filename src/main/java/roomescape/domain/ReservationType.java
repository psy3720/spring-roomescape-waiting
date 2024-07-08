package roomescape.domain;

public enum ReservationType {

    RESERVED("예약"),
    WAITING("예약대기");

    String name;

    ReservationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
