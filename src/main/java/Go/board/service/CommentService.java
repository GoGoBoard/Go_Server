package Go.board.service;

import Go.board.dto.CommentResponseDTO;
import Go.board.entity.ArticleEntity;
import Go.board.entity.CommentEntity;
import Go.board.entity.MemberEntity;
import Go.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final MemberService memberService;

    public CommentResponseDTO saveComment(String content, int postId, int memberId) {
        CommentEntity commentEntity = new CommentEntity();
        MemberEntity findMember = memberService.findMemberByMemberId(memberId);
        ArticleEntity findArticle = articleService.findByPostId(postId);
        commentEntity.setMemberId(findMember);
        commentEntity.setPostId(findArticle);
        commentEntity.setContent(content);
        commentEntity.setWriteTime(Timestamp.valueOf(LocalDateTime.now()));
        CommentEntity save = commentRepository.save(commentEntity);
        if (save == null) return null;
        CommentResponseDTO dto = CommentResponseDTO.toCommentResponseDTO(commentEntity);
        return dto;
    }

    public CommentResponseDTO updateComment(CommentEntity comment, String content) {
        comment.setContent(content);

        CommentResponseDTO dto = CommentResponseDTO.toCommentResponseDTO(comment);
        return dto;
    }

    public boolean deleteComment(int commentId, int memberId) {
        CommentEntity find = FindByCommentId(commentId);
        int findMember = find.getMemberId().getMemberId();
        if(findMember!=memberId)return false;
        commentRepository.delete(find);
        return true;
    }

    public CommentEntity FindByCommentId(int commentId) {
        CommentEntity find = commentRepository.findByCommentId(commentId);
        return find;
    }

    public List<CommentResponseDTO> getAllComment(int postId) {
        ArticleEntity find = articleService.findByPostId(postId);
        List<CommentEntity> all = commentRepository.findAllByPostId(find);
        List<CommentResponseDTO> DTOlist = new ArrayList<>(all.size());
        for (CommentEntity commentEntity : all) {
            CommentResponseDTO dto =CommentResponseDTO.toCommentResponseDTO(commentEntity);
            DTOlist.add(dto);
        }
        return DTOlist;
    }
}
