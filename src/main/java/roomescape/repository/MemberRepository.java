package roomescape.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {

    List<Member> findAll();

    Optional<Member> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value= """
        insert into member (id, email, name, password, role_id) 
        VALUES (9999 ,'admin@email.com', '관리자', '$2a$10$HGgzwqW6INvWjAfhRSQwR.fbwnfvgDlz8BPeMAepj9BUNyJO3Eu.a', 2)
    """, nativeQuery = true)
    void createAdmin();
}
