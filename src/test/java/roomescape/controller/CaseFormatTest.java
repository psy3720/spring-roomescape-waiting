package roomescape.controller;

import com.google.common.base.CaseFormat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CaseFormatTest {

    @Test
    void UPPER_CAMEL_TO_LOWER_UNDERSCORE() {
        String lowerUnderscore = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "ReservationTime");

        Assertions.assertThat(lowerUnderscore).isEqualTo("reservation_time");
    }
}
