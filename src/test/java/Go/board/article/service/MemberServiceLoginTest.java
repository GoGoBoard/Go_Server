package Go.board.article.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import Go.board.dto.MemberDTO;
import Go.board.entity.MemberEntity;
import Go.board.repository.MemberRepository;
import Go.board.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceLoginTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    public void testFindByLoginId() {
        // Given
        String loginId = "testUser";
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberId(1);
        memberEntity.setLoginId(loginId);
        memberEntity.setPassword("password");
        memberEntity.setNickname("Test User");

        // Mocking the behavior of the memberRepository.findByLoginId() method
        when(memberRepository.findByLoginId(loginId)).thenReturn(memberEntity);

        // When
        MemberDTO result = memberService.login(loginId);

        // Then
        assertEquals(memberEntity.getMemberId(), result.getMemberId());
        assertEquals(memberEntity.getLoginId(), result.getLoginId());
        assertEquals(memberEntity.getNickname(), result.getNickname());

        // Verifying that memberRepository.findByLoginId() method was called with the correct argument
        verify(memberRepository, times(1)).findByLoginId(loginId);
    }

    @Test
    public void testFindByLoginId_WithNonExistingLoginId() {
        // Given
        String loginId = "nonExistingUser";

        // Mocking the behavior of the memberRepository.findByLoginId() method
        when(memberRepository.findByLoginId(loginId)).thenReturn(null);

        // When
        MemberDTO result = memberService.login(loginId);

        // Then
        // The result should be null as there is no member with the given loginId
        assertEquals(null, result);

        // Verifying that memberRepository.findByLoginId() method was called with the correct argument
        verify(memberRepository, times(1)).findByLoginId(loginId);
    }
}
