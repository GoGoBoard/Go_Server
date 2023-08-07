package Go.board.article.dto;

import Go.board.article.entitiy.MemberEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드를 매개변수로 하는 생성자
public class MemberDTO {
    private int memberId;
    private String loginId;
    private String password;
    private String nickname;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setLoginId(memberEntity.getLoginId());
        memberDTO.setNickname(memberEntity.getNickname());
        memberDTO.setPassword(memberEntity.getPassword());
        return memberDTO;
    }
}
