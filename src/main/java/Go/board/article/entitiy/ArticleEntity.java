package Go.board.article.entitiy;

import Go.board.article.dto.ArticleDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "post")
public class ArticleEntity extends TimeEntity {
    @Id //pk컬럼 지정
    @GeneratedValue(strategy = GenerationType.AUTO)//auto_increment
    @Column
    private int postId;
    @Column
    private int memberId;
    private String title;
    private String content;

    public static ArticleEntity toSaveEntity(ArticleDTO articleDTO) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setMemberId(articleDTO.getMemberId());
        articleEntity.setPostId(articleDTO.getPostId());
        articleEntity.setContent(articleDTO.getContent());
        articleEntity.setTitle(articleDTO.getTitle());
        return articleEntity;
    }
}
