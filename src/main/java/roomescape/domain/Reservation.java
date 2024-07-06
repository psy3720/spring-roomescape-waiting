package roomescape.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String reservationDate;

    @ManyToOne
    @JoinColumn(name = "time_id")
    private ReservationTime time;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private ReservationTheme theme;

    private String status;

    public Reservation() {
    }

    public Reservation(String reservationDate, ReservationTime time, ReservationTheme theme,
                       String status, Long memberId) {
        this.reservationDate = reservationDate;
        this.time = time;
        this.theme = theme;
        this.status = status;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getTime() {
        return time;
    }

    public ReservationTheme getTheme() {
        return theme;
    }

    public String getStatus() {
        return status;
    }

    public Long getMemberId() {
        return memberId;
    }
}
