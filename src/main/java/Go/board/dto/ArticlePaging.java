package Go.board.dto;

import Go.board.entity.Post;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드를 매개변수로 하는 생성자
public class ArticlePaging{
    private String title;
    private String nickname;
    private String writeTime;

    public static ArticlePaging toarticlePagingDTO(Post post) {
        ArticlePaging articlePagingDTO = new ArticlePaging();
        articlePagingDTO.title = post.getTitle();
        articlePagingDTO.writeTime = post.getWriteTime();
        articlePagingDTO.nickname = post.getMemberId().getNickname();
        return articlePagingDTO;
    }
}

