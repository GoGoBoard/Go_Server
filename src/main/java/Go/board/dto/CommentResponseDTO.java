package Go.board.dto;

import Go.board.entity.Member;
import Go.board.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long comment_id;
    private Post post_id;
    private Member member_id;
    private String content;
    private String write_time;
}
