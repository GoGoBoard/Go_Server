package Go.board.repository;

import Go.board.entity.ArticleEntity;
import Go.board.entity.MemberEntity;
import Go.board.entity.RecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendRepository extends JpaRepository<RecommendEntity, Integer> {
    List<RecommendEntity> findByPostIdAndRecommend(ArticleEntity article, boolean like);

    Optional<RecommendEntity> findByMemberIdAndPostId(MemberEntity member, ArticleEntity article);

}
