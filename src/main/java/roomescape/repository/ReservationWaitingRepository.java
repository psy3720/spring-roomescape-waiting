package roomescape.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import roomescape.domain.Waiting;
import roomescape.dto.response.WaitingWithRank;

import java.util.List;

public interface ReservationWaitingRepository extends CrudRepository<Waiting, Long> {

    @Query("SELECT new roomescape.dto.response.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme = w.theme " +
            "       AND w2.date = w.date " +
            "       AND w2.time = w.time " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "WHERE w.memberId = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    Waiting findFirstByOrderByIdAsc();
}
