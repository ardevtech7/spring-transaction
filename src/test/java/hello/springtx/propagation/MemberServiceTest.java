package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @DisplayName("서비스 계층에 트랜잭션이 없는 경우 - 성공")
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

    /**
     * memberService - @Transactional:OFF
     * memberRepository - @Transactional:ON
     * logRepository - @Transactional:ON Exception
     */
    @DisplayName("서비스 계층에 트랜잭션이 없는 경우 - 예외")
    @Test
    void outerTxOff_fail() {
        // Given
        String username = "로그예외_outerTxOff_fail";

        // When
        assertThatThrownBy(() -> memberService.joinV1(username))
                        .isInstanceOf(RuntimeException.class);

        // Then : 모든 데이터가 정상 저장
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }
}