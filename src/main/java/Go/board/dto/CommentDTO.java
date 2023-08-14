package Go.board.dto;

import Go.board.entity.Comment;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long comment_id;
    private String content;
    private String write_time;


    public static CommentDTO toDto(Comment comment) {
        return new CommentDTO(
                comment.getComment_id(),
                comment.getContent(),
                comment.getMember_id().getNickname()
        );
    }

}
