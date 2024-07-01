package roomescape.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ReservationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String startAt;

    public ReservationTime() {

    }

    public ReservationTime(Long id) {
        this.id = id;
    }

    public ReservationTime(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(String startAt) {
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public ReservationTime toEntity(ReservationTime reservationTime, Long id) {
        return new ReservationTime(id, reservationTime.getStartAt());
    }
}
