package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.AuthFixture.로그인;
import static roomescape.fixture.MemberFixture.회원가입;
import static roomescape.fixture.ReservationFixture.예약을_생성한다;
import static roomescape.fixture.ReservationThemeFixture.예약테마를_생성한다;
import static roomescape.fixture.ReservationTimeFixture.예약시간을_생성한다;
import static roomescape.fixture.ReservationWaitingFixture.예약대기를_생성한다;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import roomescape.fixture.DateFixture;

@DisplayName("예약대기 테스트")
public class ReservationWaitingTest extends DefaultTestBase {
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "1234";
    private static final String NAME = "테스트";
    private String token;

    @BeforeEach
    void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "15:40");
        예약시간을_생성한다(params);

        params.clear();
        params.put("startAt", "16:40");
        예약시간을_생성한다(params);

        params.clear();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        예약테마를_생성한다(params);

        params.clear();
        params.put("name", "레벨3 탈출");
        params.put("description", "우테코 레벨3를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        예약테마를_생성한다(params);

        회원가입(EMAIL, PASSWORD, NAME);

        Response response = 로그인(EMAIL, PASSWORD);
        token = response.getCookie("token");

        params.clear();
        params.put("date", DateFixture.formatDate("yyyy-MM-dd", 1));
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        response = 예약을_생성한다(params, token);

        int expectedIdValue = 1;
        response.then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(expectedIdValue));
    }


    @DisplayName("예약대기를 생성한다.")
    @Test
    void reservationWaiting() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", DateFixture.formatDate("yyyy-MM-dd", 1));
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        Response response = 예약대기를_생성한다(params, token);

        response.then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("예약대기를 취소한다.")
    @Test
    void reservationWaitingDelete() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", DateFixture.formatDate("yyyy-MM-dd", 1));
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        Response response = 예약대기를_생성한다(params, token);

        long id = response.then().log().all()
                .extract()
                .body()
                .jsonPath().getLong("id");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/reservations/mine/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
