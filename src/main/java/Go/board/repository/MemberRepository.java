package Go.board.repository;

import Go.board.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    MemberEntity findByLoginId(String loginId);
    MemberEntity findByMemberId(int memberId);
}
