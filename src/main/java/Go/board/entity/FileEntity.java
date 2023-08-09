package Go.board.entity;

import javax.persistence.*;

@Entity
public class FileEntity {
    @Id
    @GeneratedValue
    private int fildId;

    @ManyToOne
    @JoinColumn(name="post_id")
    private ArticleEntity article;

    private String url; //파일저장경로
    private String fileName;//파일 원본명

}
