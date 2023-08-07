package Go.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {
    private int postId;
    private int memberId;
    private String title;
    private String content;
    private String writeTime;

    // entity는 기본 생성자를 반드시 생성해야 하지만, DTO는 그러지 않아도 괜찮다.

    // DB는 Entity로 반환하므로, DTO로 타입을 변환해야한다.
    // Post post = **Post entity**에 있는 정보를 PostDTO에 맵핑하는 중
    // 실제 화면에서 사용할 것만 post entity에서 가져오면 된다
    public PostDTO(Integer postId, Integer memberId, String title, String content, String writeTime) {
        this.postId = postId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.writeTime = writeTime;
    }

}
