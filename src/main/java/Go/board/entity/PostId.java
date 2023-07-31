package Go.board.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class PostId implements Serializable {
    private int postId;
    private int memberId;
}
