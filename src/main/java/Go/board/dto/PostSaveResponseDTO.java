package Go.board.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSaveResponseDTO {
    private String title;
    private String content;
}
