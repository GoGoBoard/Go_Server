package Go.board.repository;

import Go.board.entity.ArticleEntity;
import Go.board.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    List<FileEntity> findAllByArticle(ArticleEntity article);
    void deleteAllByArticle(ArticleEntity article);

}
