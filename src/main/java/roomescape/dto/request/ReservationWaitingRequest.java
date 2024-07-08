package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationWaitingRequest {

    @NotBlank(message = "날짜가 입력되지 않았습니다.")
    String date;

    @NotNull(message = "테마가 선택되지 않았습니다.")
    Long themeId;

    @NotNull(message = "예약 시간이 선택되지 않았습니다.")
    Long timeId;

    public ReservationWaitingRequest() {
    }

    public ReservationWaitingRequest(String date, Long themeId, Long timeId) {
        this.date = date;
        this.themeId = themeId;
        this.timeId = timeId;
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
