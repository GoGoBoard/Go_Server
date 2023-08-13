package Go.board.entity;

import Go.board.dto.ArticleResponseDTO;
import Go.board.dto.ArticleSaveDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "post")
public class ArticleEntity extends TimeEntity {
    @Id //pk컬럼 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment
    @Column(name = "post_id", updatable = false)
    private int postId;
    private String title;
    private String content;
    //게시글:사용자 다대일
    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;
    //게시글:첨부파일 일대다
    @OneToMany(
            mappedBy = "article",
            cascade = {CascadeType.MERGE, CascadeType.REMOVE},//영속성 전이
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<FileEntity> files = new ArrayList<>();

    //article에서 파일 처리
    public void addFile(FileEntity fileEntity) {
        this.files.add(fileEntity);
        if (fileEntity.getArticle() != this)
            fileEntity.setArticle(this);
    }

    public void setMember(MemberEntity member) {
        this.member = member;
        if (!member.getArticles().contains(this)) {
            member.getArticles().add(this);
        }
    }

    public static ArticleEntity toSaveEntity(ArticleResponseDTO articleResponseDTO) {
        ArticleEntity articleEntity = new ArticleEntity();
        //  articleEntity.setMemberId(articleResponseDTO.getMemberId());
        articleEntity.setContent(articleResponseDTO.getContent());
        articleEntity.setTitle(articleResponseDTO.getTitle());
        articleEntity.setWriteTime(articleResponseDTO.getWriteTime());
        return articleEntity;
    }

    public static ArticleEntity toArticleEntity(ArticleSaveDTO articleSaveDTO) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle(articleSaveDTO.getTitle());
        articleEntity.setContent(articleSaveDTO.getContent());
        return articleEntity;
    }

    @Override
    public String toString() {
        return "ArticleEntity{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
