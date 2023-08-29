package Go.board.repository;

import Go.board.entity.Member;
import Go.board.entity.Post;
import Go.board.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    Optional<Recommend> findByMemberAndBoard(Member member, Post post);
}
