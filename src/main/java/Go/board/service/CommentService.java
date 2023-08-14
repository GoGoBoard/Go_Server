package Go.board.service;

import Go.board.entity.ArticleEntity;
import Go.board.entity.CommentEntity;
import Go.board.entity.MemberEntity;
import Go.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final MemberService memberService;

    public boolean saveComment(String content, int postId, int memberId) {
        CommentEntity commentEntity = new CommentEntity();
        MemberEntity findMember = memberService.findMemberByMemberId(memberId);
        ArticleEntity findArticle = articleService.findByPostId(postId);
        commentEntity.setMemberId(findMember);
        commentEntity.setPostId(findArticle);
        commentEntity.setContent(content);
        commentEntity.setWriteTime(LocalDateTime.now());
        CommentEntity save = commentRepository.save(commentEntity);
        if (save == null) return false;
        return true;
    }
}
