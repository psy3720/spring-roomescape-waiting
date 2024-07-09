package roomescape.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository extends CrudRepository<ReservationTime, Long> {

    List<ReservationTime> findAll();

    Long countByStartAt(String startAt);

    @Query(value = """ 
            SELECT rt.*
            FROM reservation_time rt
            WHERE NOT EXISTS (
                SELECT 1 FROM reservation r 
                WHERE r.reservation_date = :reservationDate
                AND r.theme_id = :themeId AND r.time_id = rt.id
            )
            """, nativeQuery = true)
    List<ReservationTime> findAvailableTimes(@Param("reservationDate") String reservationDate
            , @Param("themeId") Long themeId);

    @Query(value = """
            SELECT rt.*
            FROM reservation_time rt
            INNER JOIN reservation r ON r.time_id = rt.id
            WHERE r.reservation_date = :reservationDate
            AND r.theme_id = :themeId
            AND r.member_id = :memberId
            """, nativeQuery = true)
    List<ReservationTime> existReservationTimes(@Param("reservationDate") String reservationDate,
                                                @Param("themeId") Long themeId, @Param("memberId") Long memberId);

    @Query(value = """
            SELECT rt.*
            FROM reservation_time rt
            INNER JOIN waiting w ON w.time_id = rt.id
            WHERE w.date = :reservationDate
            AND w.theme_id = :themeId
            AND w.member_id = :memberId
            """, nativeQuery = true)
    List<ReservationTime> existWaitingTimes(@Param("reservationDate") String reservationDate,
                                            @Param("themeId") Long themeId, @Param("memberId") Long memberId);
}
