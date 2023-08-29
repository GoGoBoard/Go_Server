package Go.board.repository;

import Go.board.entity.Member;
import Go.board.entity.Post;
import Go.board.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    Optional<Recommend> findByMemberAndPost(Member member, Post post);
    List<Recommend> findByPostAndRecommend(Post post, boolean like);
}
