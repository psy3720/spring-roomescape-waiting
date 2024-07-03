package roomescape.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import roomescape.domain.ReservationTheme;

public interface ReservationThemeRepository extends CrudRepository<ReservationTheme, Long> {

    long countByName(String name);

    void deleteById(Long id);

    List<ReservationTheme> findAll();
}
