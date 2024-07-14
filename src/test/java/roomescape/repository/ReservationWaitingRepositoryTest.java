package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
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
import roomescape.dto.response.WaitingWithRank;

@DataJpaTest
class ReservationWaitingRepositoryTest {

    @Autowired
    ReservationWaitingRepository reservationWaitingRepository;

    @Autowired
    ReservationThemeRepository reservationThemeRepository;

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init() {
        Member member1 = new Member(1L, "테스트1", "test1@email.com", "1234");
        memberRepository.save(member1);
        Member member2 = new Member(2L, "테스트2", "test2@email.com", "1234");
        memberRepository.save(member2);

        ReservationTheme reservationTheme = new ReservationTheme(1L, "테마", "설명1", "썸네일1");
        reservationThemeRepository.save(reservationTheme);

        ReservationTime reservationTime = new ReservationTime(1L, "18:00");
        reservationTimeRepository.save(reservationTime);

        Reservation reservation = new Reservation("18:00"
                , reservationTime
                , reservationTheme
                , ReservationType.RESERVED.getName()
                , 1L);
        reservationRepository.save(reservation);
    }

    @PersistenceContext
    EntityManager entityManager;

    @AfterEach
    void tearDown() {
        entityManager.createNativeQuery("TRUNCATE TABLE waiting").executeUpdate();
    }

    @DisplayName("회원아이디로 예약대기 정보와 예약대기 순서 번호를 가져온다.")
    @Test
    void findWaitingsWithRankByMemberId() {
        Waiting waiting = new Waiting("2025-01-01", 1L, 1L, 2L);

        reservationWaitingRepository.save(waiting);

        List<WaitingWithRank> findWaiting = reservationWaitingRepository.findWaitingsWithRankByMemberId(
                2L);

        assertAll(
                () -> assertThat(findWaiting.size()).isEqualTo(1)
                , () -> assertThat(findWaiting.get(0).getRank()).isEqualTo(0)
                , () -> assertThat(findWaiting.get(0).getWaiting().getMemberId()).isEqualTo(2L)
                , () -> assertThat(findWaiting.get(0).getWaiting().getDate()).isEqualTo("2025-01-01")
                , () -> assertThat(findWaiting.get(0).getWaiting().getTimeId()).isEqualTo(1L)
                , () -> assertThat(findWaiting.get(0).getWaiting().getThemeId()).isEqualTo(1L)
        );

    }
}
