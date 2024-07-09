package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

public class ReservationWaitingFixture {

    public static Response 예약대기를_생성한다(Map<String, Object> params, String token) {
        return RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations/waiting");
    }
}
