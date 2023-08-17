package Go.board.repository;

import Go.board.entity.ArticleEntity;
import Go.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Integer> {
    CommentEntity findByCommentId(int commentId);
    List<CommentEntity> findAllByPostId(ArticleEntity article);
}
