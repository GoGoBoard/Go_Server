package Go.board.dto;

import Go.board.entity.ArticleEntity;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드를 매개변수로 하는 생성자
public class ArticlePagingDTO {
    private int postId;
    private String title;
    private String nicknmae;
    private Timestamp localDateTime;

    public static ArticlePagingDTO toarticlePagingDTO(ArticleEntity articleEntity) {
        ArticlePagingDTO articlePagingDTO = new ArticlePagingDTO();
        articlePagingDTO.postId = articleEntity.getPostId();
        articlePagingDTO.title = articleEntity.getTitle();
        articlePagingDTO.localDateTime = articleEntity.getWriteTime();
        articlePagingDTO.nicknmae = articleEntity.getMember().getNickname();
        return articlePagingDTO;
    }
}
