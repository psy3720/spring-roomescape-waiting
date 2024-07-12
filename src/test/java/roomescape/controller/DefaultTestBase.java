package roomescape.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        memberRepository.createAdmin();
    }
}
