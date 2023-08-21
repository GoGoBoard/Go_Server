package Go.board.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private int postId;
    private Long memberId;
    private String title;
    private String content;
    private String writeTime;


}
