package Go.board.service;

import Go.board.dto.JoinRequest;
import Go.board.dto.LoginRequest;
import Go.board.entity.Member;
import Go.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * loginId 중복 체크
     * 회원가입 기능 구현 시 사용
     * 중복되면 true return
     */
    public boolean checkLoginIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    /**
     * nickname 중복 체크
     * 회원가입 기능 구현 시 사용
     * 중복되면 true return
     */
    public boolean checkNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    /**
     * 회원가입 기능
     * 화면에서 JoinRequest(loginId, password, nickname)을 입력받아 Member로 변환 후 저장
     * 비밀번호를 암호화해서 저장
     * loginId, nickname 중복 체크는 Controller에서 진행 => 에러 메세지 출력을 위해
     */
    public void join(JoinRequest req) {
        memberRepository.save(req.toEntity(passwordEncoder.encode(req.getPassword())));
    }

    /**
     *  로그인 기능
     *  화면에서 LoginRequest(loginId, password)을 입력받아 loginId와 password가 일치하면 Member return
     *  loginId가 존재하지 않거나 password가 일치하지 않으면 null return
     */
    public Member login(LoginRequest req) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(req.getLoginId());

        // loginId와 일치하는 Member가 없으면 null return
        if(optionalMember.isEmpty()) {
            return null;
        }

        Member member = optionalMember.get();

        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            return null;
        }

        return member;
    }

    /**
     * userId(Long)를 입력받아 Member를 return 해주는 기능
     * 인증, 인가 시 사용
     * userId가 null이거나(로그인 X) userId로 찾아온 Member가 없으면 null return
     * userId로 찾아온 Member가 존재하면 Member return
     */
    public Member getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if(optionalMember.isEmpty()) return null;

        return optionalMember.get();
    }

    /**
     * loginId(String)를 입력받아 Member를 return 해주는 기능
     * 인증, 인가 시 사용
     * loginId가 null이거나(로그인 X) userId로 찾아온 Member가 없으면 null return
     * loginId로 찾아온 Member가 존재하면 Member return
     */
    public Member getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        if(optionalMember.isEmpty()) return null;

        return optionalMember.get();
    }
}