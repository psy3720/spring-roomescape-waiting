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
            SELECT rt.* FROM reservation_time rt
            WHERE NOT EXISTS (
                SELECT 1 FROM reservation r 
                WHERE r.reservation_date = :reservationDate
                AND r.theme_id = :themeId AND r.time_id = rt.id
            )
            """, nativeQuery = true)
    List<ReservationTime> findAvailableTimes(@Param("reservationDate") String reservationDate
            , @Param("themeId") Long themeId);
}
