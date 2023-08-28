package Go.board.service;

import Go.board.Handler.FileHandler;
import Go.board.repository.AttachmentRepository;
import Go.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AttachmentService {
    private final PostRepository postRepository;
    private final AttachmentRepository attachmentRepository;

}
