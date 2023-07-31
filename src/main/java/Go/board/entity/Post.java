package Go.board.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "post")
@IdClass(PostId.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    @Id
    private int memberId;
    private String title;
    private String content;
    private String writeTime;
}
