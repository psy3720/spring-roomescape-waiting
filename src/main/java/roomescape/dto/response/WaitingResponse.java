package roomescape.dto.response;

public class WaitingResponse {

    private Long id;
    private String date;
    private Long themeId;
    private Long timeId;

    public WaitingResponse(Long id, String date, Long themeId, Long timeId) {
        this.id = id;
        this.date = date;
        this.themeId = themeId;
        this.timeId = timeId;
    }

    public WaitingResponse() {
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
}
