package Go.board.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleSaveDTO {
    private String title;
    private String content;
    private List<MultipartFile> files;


}
