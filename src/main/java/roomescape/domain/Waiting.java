package roomescape.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String date;

    Long themeId;

    Long timeId;

    Long memberId;

    public Waiting() {
    }

    public Waiting(String date, Long themeId, Long timeId, Long memberId) {
        this.date = date;
        this.themeId = themeId;
        this.timeId = timeId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
