package roomescape.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import roomescape.domain.Reservation;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    List<Reservation> findAll();

    long countByReservationDateAndTimeIdAndThemeId(String date, Long timeId, Long themeId);

    long countByThemeId(Long id);

    long countByTimeId(Long id);

    List<Reservation> findAllByMemberId(Long memberId);
}
