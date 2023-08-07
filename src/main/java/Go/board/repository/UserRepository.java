package Go.board.repository;

import Go.board.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member, Integer> {
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<Member> findByLoginId(String loginId);
}