package roomescape.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationType;
import roomescape.domain.Waiting;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReservationTimeRepositoryTest {

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @Autowired
    ReservationThemeRepository reservationThemeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationWaitingRepository reservationWaitingRepository;

    @PersistenceContext
    EntityManager entityManager;

    private static final int DEFAULT_TIME_SIZE = 3;

    @BeforeEach
    void init() {
        ReservationTime reservationTime1 = new ReservationTime(1L, "18:00");
        ReservationTime reservationTime2 = new ReservationTime(2L, "19:00");
        ReservationTime reservationTime3 = new ReservationTime(3L, "20:00");
        reservationTimeRepository.save(reservationTime1);
        reservationTimeRepository.save(reservationTime2);
        reservationTimeRepository.save(reservationTime3);

        ReservationTheme reservationTheme1 = new ReservationTheme(1L, "테마1", "설명1", "썸네일1");
        ReservationTheme reservationTheme2 = new ReservationTheme(2L, "테마2", "설명2", "썸네일2");
        ReservationTheme reservationTheme3 = new ReservationTheme(3L, "테마3", "설명3", "썸네일3");
        reservationThemeRepository.save(reservationTheme1);
        reservationThemeRepository.save(reservationTheme2);
        reservationThemeRepository.save(reservationTheme3);

        Member member1 = new Member(1L, "회원1");
        Member member2 = new Member(2L, "회원2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();

        Reservation reservation = new Reservation("2025-07-14"
                , reservationTime1
                , reservationTheme1
                , ReservationType.RESERVED.getName()
                , 1L);
        reservationRepository.save(reservation);

        Waiting waiting = new Waiting("2025-07-14", reservationTheme1, reservationTime1, 2L);
        reservationWaitingRepository.save(waiting);
    }

    @AfterEach
    public void tearDown() {
        entityManager.createNativeQuery("TRUNCATE TABLE waiting").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE reservation").executeUpdate();
    }

    @DisplayName("예약가능 시간 목록을 조회한다.")
    @Test
    void findAvailableTimes() {
        String reservationDate = "2025-07-14";
        long themeId = 1L;
        List<ReservationTime> availableTimes = reservationTimeRepository.findAvailableTimes(reservationDate, themeId);

        assertThat(availableTimes.size()).isEqualTo(2 + DEFAULT_TIME_SIZE);
    }

    @DisplayName("이미 존재하는 예약 시간목록을 조회한다.")
    @Test
    void existReservationTimes() {
        long memberId = 1L;
        long themeId = 1L;
        String reservationDate = "2025-07-14";

        List<ReservationTime> existReservationTimes = reservationTimeRepository.existReservationTimes(
                reservationDate,
                themeId,
                memberId);

        assertThat(existReservationTimes.size()).isEqualTo(1);
    }

    @DisplayName("이미 존재하는 예약대기 시간목록을 조회한다.")
    @Test
    void existWaitingTimes() {
        String reservationDate = "2025-07-14";
        long themeId = 1L;
        long memberId = 2L;

        List<ReservationTime> existWaitingTimes = reservationTimeRepository.existWaitingTimes(reservationDate, themeId,
                memberId);

        assertThat(existWaitingTimes.size()).isEqualTo(1);
    }
}
