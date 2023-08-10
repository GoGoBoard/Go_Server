package Go.board.dto;

import Go.board.entity.ArticleEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//DTO(data transfer Object)
@Getter
@Setter
@ToString
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드를 매개변수로 하는 생성자
public class ArticleDTO {
    private int postId;
    private int memberId;
    private String title;
    private String content;
    private LocalDateTime writeTime;
    private List<MultipartFile> files = new ArrayList<>();

    public ArticleDTO(int memberId, String title, String content, LocalDateTime writeTime) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.writeTime = writeTime;
    }

    public static ArticleDTO toarticleDTO(ArticleEntity articleEntity) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setPostId(articleEntity.getPostId());
        articleDTO.setMemberId(articleEntity.getMemberId());
        articleDTO.setTitle(articleEntity.getTitle());
        articleDTO.setContent(articleEntity.getContent());
        articleDTO.setWriteTime(articleEntity.getWriteTime());
        return articleDTO;
    }

}
