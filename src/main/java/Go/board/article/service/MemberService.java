package Go.board.article.service;

import Go.board.article.dto.MemberDTO;
import Go.board.article.entitiy.MemberEntity;
import Go.board.article.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberDTO login(String loginId) {
        //db에서  로그인 id를 찾고
        MemberEntity findMemberEntity = memberRepository.findByLoginId(loginId);
        //dto로 컨버팅한 것을 return
        if (findMemberEntity != null) {
            return MemberDTO.toomemberDTO(findMemberEntity);
        } else return null;

    }

    public boolean registerMember(MemberDTO memberDTO) {
        MemberEntity findMemberEntity = memberRepository.findByLoginId(memberDTO.getLoginId());
        if (findMemberEntity != null) {
            return false;
        } else {
            memberRepository.save(MemberEntity.toSaveMember(memberDTO));
            return true;
        }

    }
}
