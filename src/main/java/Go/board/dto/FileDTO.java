package Go.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FileDTO {
    public int id;
    public int postId;
    public String url;
    public String fileName;

}
