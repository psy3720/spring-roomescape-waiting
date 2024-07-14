package roomescape.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import roomescape.domain.Waiting;
import roomescape.dto.response.WaitingWithRank;

public interface ReservationWaitingRepository extends CrudRepository<Waiting, Long> {

    @Query("SELECT new roomescape.dto.response.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.themeId = w.themeId " +
            "       AND w2.date = w.date " +
            "       AND w2.timeId = w.timeId " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "WHERE w.memberId = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

}
