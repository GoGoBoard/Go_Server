package Go.board.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int postId;
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member memberId;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "write_time")
    private Long writeTime;

    @OneToMany(
            mappedBy = "post",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Attachment> attachment = new ArrayList<>();

    // Post 에서 파일 처리 위함
    public void addPhoto(Attachment attachment) {
        this.attachment.add(attachment);

        // 게시글에 파일이 저장되어있지 않은 경우
        if(attachment.getPost() != this)
            // 파일 저장
            attachment.setPost(this);
    }

}
