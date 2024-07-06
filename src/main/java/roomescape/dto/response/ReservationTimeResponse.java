package roomescape.dto.response;

public class ReservationTimeResponse {

    private Long id;
    private String startAt;
    private boolean alreadyBooked;

    public ReservationTimeResponse() {
    }

    public ReservationTimeResponse(Long id, String startAt, boolean alreadyBooked) {
        this.id = id;
        this.startAt = startAt;
        this.alreadyBooked = alreadyBooked;
    }

    public ReservationTimeResponse(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public boolean isAlreadyBooked() {
        return alreadyBooked;
    }
}
