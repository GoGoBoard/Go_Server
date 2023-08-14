package Go.board.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long comment_id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post_id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member_id;

    @Column(name = "content")
    private String content;

    @Column(name = "write_time")
    private String write_time;

}
