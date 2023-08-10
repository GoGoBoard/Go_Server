package Go.board.entity;

import Go.board.dto.ArticleDTO;
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
    @Column(name="post_id",updatable = false)
    private int postId;
    @Column(name="member_id")
    private int memberId;
    private String title;
    private String content;
    //게시글:사용자 다대일
    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private MemberEntity member;
    //게시글:첨부파일 일대다
    @OneToMany(
            mappedBy = "article",
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE},//영속성 전이
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<FileEntity> files = new ArrayList<>();
    //article에서 파일 처리
    public void addFile(FileEntity fileEntity){
        System.out.println("여기까진아오나요");
        this.files.add(fileEntity);
        if(fileEntity.getArticle()!=this)
            fileEntity.setArticle(this);
    }

    public static ArticleEntity toSaveEntity(ArticleDTO articleDTO) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setMemberId(articleDTO.getMemberId());
        articleEntity.setContent(articleDTO.getContent());
        articleEntity.setTitle(articleDTO.getTitle());
        articleEntity.setWriteTime(articleDTO.getWriteTime());
        return articleEntity;
    }
}
