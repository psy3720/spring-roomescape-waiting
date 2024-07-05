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

    private String name;

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

    public Reservation(Long id, String name, String reservationDate, ReservationTime time, ReservationTheme theme,
                       String status) {
        this.id = id;
        this.name = name;
        this.reservationDate = reservationDate;
        this.time = time;
        this.theme = theme;
        this.status = status;
    }

    public Reservation(String name, Long memberId, String reservationDate, ReservationTime time, ReservationTheme theme,
                       String status) {
        this.name = name;
        this.memberId = memberId;
        this.reservationDate = reservationDate;
        this.time = time;
        this.theme = theme;
        this.status = status;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public Reservation toEntity(Reservation reservation, Long id) {
        return new Reservation(id
                , reservation.getName()
                , reservation.getReservationDate()
                , reservation.getTime()
                , reservation.getTheme()
                , reservation.getStatus());
    }
}
