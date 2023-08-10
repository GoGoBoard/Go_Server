package Go.board.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "file")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private int fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", updatable = false)
    private ArticleEntity article;

    private String url; //파일저장경로
    private String fileName;//파일 원본명

    public FileEntity(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    //Article정보 저장
    public void setArticle(ArticleEntity articleEntity) {
        this.article = articleEntity;
        //게시글에 현재 파일이 존재하지 않으면 파일 추가
        if (!articleEntity.getFiles().contains(this))
            articleEntity.getFiles().add(this);
    }
}
