package Go.board.entity;

import Go.board.dto.MemberDTO;
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
    @Column(name = "member_id")
    private int memberId;
    @Column(name = "login_id")
    private String loginId;
    @Column
    private String password;
    @Column
    private String nickname;
    //사용자:게시글 일대다
   /* @OneToMany(
            mappedBy = "member",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},//영속성 전이
            orphanRemoval = true
    )//다대일 단방향으로도 해보자
    private List<ArticleEntity> articles = new ArrayList<>();*/

   /* public void addArticle(ArticleEntity article) {
        this.articles.add(article);
        if (article.getMember() != this) {
            article.setMember(this);
        }
    }*/

    public static MemberEntity toSaveMember(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberId(memberDTO.getMemberId());
        memberEntity.setNickname(memberDTO.getNickname());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setLoginId(memberDTO.getLoginId());
        return memberEntity;
    }

    @Override
    public String toString() {
        return "MemberEntity{" +
                "memberId=" + memberId +
                ", loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
