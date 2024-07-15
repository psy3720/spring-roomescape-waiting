package roomescape.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.domain.RoleType;
import roomescape.repository.MemberRepository;
import roomescape.util.DatabaseCleanup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DefaultTestBase {

    @Autowired
    private DatabaseCleanup dbDatabaseCleanup;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void cleanUp() {
        dbDatabaseCleanup.execute();

        createAdmin();
    }

    private void createAdmin() {
        Member admin = new Member(
                9999L
                , "관리자"
                , "admin@email.com"
                , "$2a$10$HGgzwqW6INvWjAfhRSQwR.fbwnfvgDlz8BPeMAepj9BUNyJO3Eu.a"
                , new Role(2L, RoleType.ADMIN));
        memberRepository.save(admin);
    }
}
