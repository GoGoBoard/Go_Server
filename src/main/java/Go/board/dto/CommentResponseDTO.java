package Go.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentResponseDTO {
    private String nickname;
    private String content;
    private LocalDateTime localDateTime;
}
