package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;

    /**
     * memberService - @Transactional:OFF
     * memberRepository - @Transactional:ON
     * logRepository - @Transactional:ON
     */
    @DisplayName("밖에 트랜잭션이 없는 경우")
    @Test
    void outerTxOff_success() {
        // Given
        String username = "outerTxOff_success";

        // When
        memberService.joinV1(username);

        // Then : 모든 데이터가 정상 저장
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }
}