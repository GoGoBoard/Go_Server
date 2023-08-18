package Go.board.dto;

import Go.board.entity.ArticleEntity;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//DTO(data transfer Object)
@Getter
@Setter
@ToString
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드를 매개변수로 하는 생성자
public class ArticleResponseDTO {
    private int postId;
    private String nickname;
    private String title;
    private String content;
    private Timestamp writeTime;
    private List<String> filePathList = new ArrayList<>();
    private List<CommentResponseDTO> comments = new ArrayList<>();

    public static ArticleResponseDTO toarticleResponseDTO(ArticleEntity articleEntity) {
        ArticleResponseDTO articleResponseDTO = new ArticleResponseDTO();
        articleResponseDTO.setPostId(articleEntity.getPostId());
        articleResponseDTO.setNickname(articleEntity.getMember().getNickname());
        articleResponseDTO.setTitle(articleEntity.getTitle());
        articleResponseDTO.setContent(articleEntity.getContent());
        articleResponseDTO.setWriteTime(articleEntity.getWriteTime());
        return articleResponseDTO;
    }

}
