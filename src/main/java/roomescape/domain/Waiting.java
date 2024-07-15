package roomescape.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String date;

    @ManyToOne
    ReservationTheme theme;

    @ManyToOne
    ReservationTime time;

    Long memberId;

    public Waiting() {
    }

    public Waiting(String date, ReservationTheme theme, ReservationTime time, Long memberId) {
        this.date = date;
        this.theme = theme;
        this.time = time;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public ReservationTheme getTheme() {
        return theme;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Long getMemberId() {
        return memberId;
    }
}
