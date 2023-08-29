package Go.board.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostSaveResponseDTO {
    private String title;
    private String content;
    private List<String> filePathList = new ArrayList<>();
}
