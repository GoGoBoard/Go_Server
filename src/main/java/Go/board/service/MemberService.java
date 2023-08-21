package Go.board.service;

import Go.board.dto.LoginDTO;
import Go.board.dto.MemberResponseDTO;
import Go.board.dto.RegisterDTO;
import Go.board.entity.MemberEntity;
import Go.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberEntity findMemberByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public MemberEntity findMemberByMemberId(int memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    public MemberEntity login(LoginDTO loginDTO) {
        //db에서  로그인 id를 찾고
        MemberEntity findMemberEntity = findMemberByLoginId(loginDTO.getLoginId());
        if (findMemberEntity != null) {
            String encodePW = findMemberEntity.getPassword();
            if (passwordEncoder.matches(loginDTO.getPassword(), encodePW)) {
                return findMemberEntity;
            }
            else return null;
        } else return null;
    }

    public MemberResponseDTO registerMember(RegisterDTO registerDTO) {
        MemberEntity findMemberEntity = findMemberByLoginId(registerDTO.getLoginId());
        if (findMemberEntity != null) {//해당 로그인아이디 존재
            return null;
        } else {
            String password = registerDTO.getPassword();
            registerDTO.setPassword(passwordEncoder.encode(password));
            MemberEntity save = memberRepository.save(RegisterDTO.toEntity(registerDTO));
            return MemberResponseDTO.toMemberResponseDTO(save);
        }

    }
}
