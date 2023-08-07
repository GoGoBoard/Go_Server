package Go.board.service;

import Go.board.dto.MemberDTO;
import Go.board.entity.MemberEntity;
import Go.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberEntity findMemberByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public MemberDTO login(String loginId, String password) {
        //db에서  로그인 id를 찾고
        MemberEntity findMemberEntity = findMemberByLoginId(loginId);

        if (findMemberEntity != null && findMemberEntity.getPassword().equals(password)) {
            return MemberDTO.toMemberDTO(findMemberEntity);//dto로 컨버팅한 것을 return
        } else return null;
    }

    public boolean registerMember(MemberDTO memberDTO) {
        MemberEntity findMemberEntity = findMemberByLoginId(memberDTO.getLoginId());
        if (findMemberEntity != null) {//해당 로그인아이디 존재
            return false;
        } else {
            memberRepository.save(MemberEntity.toSaveMember(memberDTO));
            return true;
        }

    }
}
