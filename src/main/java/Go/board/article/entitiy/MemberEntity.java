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
    @Column
    private int memberId;
    @Column
    private String loginId;
    @Column
    private String password;
    @Column
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
