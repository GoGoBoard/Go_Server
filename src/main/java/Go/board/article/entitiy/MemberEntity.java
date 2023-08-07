package Go.board.article.entitiy;

import Go.board.article.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue
    @Column(name = "memberId")
    private int memberId;
    @Column(name = "loginId")
    private String loginId;
    @Column(name = "password")
    private String password;
    @Column(name = "nickname")
    private String nickname;

    public static MemberEntity toSaveMember(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberId(memberDTO.getMemberId());
        memberEntity.setNickname(memberDTO.getNickname());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setLoginId(memberDTO.getLoginId());
        return memberEntity;
    }

}
