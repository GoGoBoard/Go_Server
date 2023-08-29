package Go.board.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class AttachmentDTO {
    private String filePath;
    private String fileName;

    @Builder
    public AttachmentDTO(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
