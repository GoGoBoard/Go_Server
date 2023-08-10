package Go.board.entity;

import Go.board.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue
    @Column(name="member_id")
    private int memberId;
    @Column(name="login_id")
    private String loginId;
    @Column
    private String password;
    @Column
    private String nickname;
    //사용자:게시글 일대다
    @OneToMany(
            mappedBy = "member",
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE},//영속성 전이
            orphanRemoval = true
    )
    List<ArticleEntity> articles = new ArrayList<>();

    public static MemberEntity toSaveMember(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberId(memberDTO.getMemberId());
        memberEntity.setNickname(memberDTO.getNickname());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setLoginId(memberDTO.getLoginId());
        return memberEntity;
    }

}
