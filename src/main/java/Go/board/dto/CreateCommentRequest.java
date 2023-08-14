package Go.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {
    private int postId;
    private Long memberId;
    private String content;
}
