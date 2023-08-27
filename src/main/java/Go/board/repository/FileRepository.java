package Go.board.repository;

import Go.board.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Attachment, Long> {
}
