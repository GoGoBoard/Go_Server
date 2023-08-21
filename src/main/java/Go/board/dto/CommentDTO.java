package Go.board.dto;

import Go.board.entity.Comment;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String nickname;
    private String content;
    private String write_time;


    public static CommentDTO toDto(Comment comment) {
        return new CommentDTO(
                comment.getMember_id().getNickname(),
                comment.getContent(),
                comment.getWrite_time()
        );
    }

}
