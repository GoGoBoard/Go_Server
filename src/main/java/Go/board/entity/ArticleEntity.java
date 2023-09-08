package Go.board.entity;

import Go.board.dto.ArticleRequestDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "post")
public class ArticleEntity extends TimeEntity {
    @Id //pk컬럼 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment
    @Column(name = "post_id", updatable = false)
    private int postId;
    private String title;
    private String content;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;
    public static ArticleEntity toArticleEntity(ArticleRequestDTO articleRequestDTO) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle(articleRequestDTO.getTitle());
        articleEntity.setContent(articleRequestDTO.getContent());
        return articleEntity;
    }

    @Override
    public String toString() {
        return "ArticleEntity{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
