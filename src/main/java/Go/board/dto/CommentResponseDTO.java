package Go.board.dto;

import Go.board.entity.CommentEntity;
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

    public CommentResponseDTO toCommentResponseDTO(CommentEntity commentEntity){
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setNickname(commentEntity.getMemberId().getNickname());
        dto.setContent(commentEntity.getContent());
        dto.setLocalDateTime(commentEntity.getWriteTime());

        return dto;
    }
}
