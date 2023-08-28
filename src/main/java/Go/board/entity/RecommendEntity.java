package Go.board.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "recommend")
@Getter
@Setter
@NoArgsConstructor
public class RecommendEntity {
    //게시글<->추천 일대다
    //사용자<->추천 일대다
    @Id
    @GeneratedValue
    @Column(name = "rec_id")
    private int recId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private ArticleEntity postId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberId;

    private boolean recommend;
}
