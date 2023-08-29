package Go.board.dto;

import Go.board.entity.Comment;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private String nickname;
    private String content;
    private Long writeTime;


    public static CommentResponse toDto(Comment comment) {
        return new CommentResponse(
                comment.getMember_id().getNickname(),
                comment.getContent(),
                comment.getWriteTime()
        );
    }

}
