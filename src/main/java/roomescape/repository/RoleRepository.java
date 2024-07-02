package roomescape.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import roomescape.domain.Role;
import roomescape.domain.RoleType;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
