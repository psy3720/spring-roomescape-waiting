package roomescape.controller;

import io.restassured.response.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import roomescape.fixture.DateFixture;
import roomescape.repository.MemberRepository;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.AuthFixture.로그인;
import static roomescape.fixture.MemberFixture.회원가입;
import static roomescape.fixture.ReservationFixture.예약을_생성한다_관리자;
import static roomescape.fixture.ReservationThemeFixture.예약테마를_생성한다;
import static roomescape.fixture.ReservationTimeFixture.예약시간을_생성한다;

@DisplayName("관리자 예약 테스트")
public class ReservationAdminTest extends DefaultTestBase {
    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String PASSWORD = "1234";
    private static final String NAME = "관리자";
    private String token;

    @BeforeEach
    void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "15:40");

        예약시간을_생성한다(params);

        params.clear();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        예약테마를_생성한다(params);

        Response response = 로그인(ADMIN_EMAIL, PASSWORD);
        token = response.getCookie("token");
    }

    @Test
    @DisplayName("관리자 계정으로 예약을 생성한다.")
    void createAdminReservation() {
        Response signupResponse = 회원가입("test@email.com", PASSWORD, "테스트");
        long memberId = signupResponse.then().log().all()
                .extract()
                .body()
                .jsonPath()
                .getLong("id");

        Map<String, Object> params = new HashMap<>();
        params.put("date", DateFixture.formatDate("yyyy-MM-dd", 1));
        params.put("timeId", 1L);
        params.put("themeId", 1L);
        params.put("memberId", memberId);

        Response response = 예약을_생성한다_관리자(params, token);

        int expectedIdValue = 1;
        response.then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(expectedIdValue));
    }
}
