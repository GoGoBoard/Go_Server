package Go.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "file")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Attachment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // 첨부된 게시글

    @Column(name = "url", nullable = false)
    private String filePath;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Builder
    public Attachment(String fileName, String filePath) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    // 게시글 정보 저장
    public void setPost(Post post){
        this.post = post;

        // 게시글에 현재 파일이 존재하지 않는다면
        if(!post.getAttachment().contains(this))
            // 파일 추가
            post.getAttachment().add(this);
    }
}
