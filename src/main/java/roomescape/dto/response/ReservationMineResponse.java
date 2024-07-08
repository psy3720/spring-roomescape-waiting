package roomescape.dto.response;

public class ReservationMineResponse {

    private Long reservationId;
    private String theme;
    private String date;
    private String time;
    private String status;
    private Long rank;

    public ReservationMineResponse() {
    }

    public ReservationMineResponse(Long reservationId, String theme, String date, String time, String status,
                                   Long rank) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
        this.rank = rank;
    }

    public ReservationMineResponse(Long reservationId, String theme, String date, String time, String status) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public Long getRank() {
        return rank;
    }
}
