package Go.board.service;

import Go.board.dto.CommentDTO;
import Go.board.entity.Comment;
import Go.board.entity.Member;
import Go.board.entity.Post;
import Go.board.repository.CommentRepository;
import Go.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentDTO writeComment(int postId, CommentDTO commentDTO, Member member) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());

        // 게시판 번호로 게시글 찾기
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            return new IllegalArgumentException("게시판을 찾을 수 없습니다.");
        });

        comment.setMember_id(member);
        comment.setPost_id(post);
        commentRepository.save(comment);

        return CommentDTO.toDto(comment);
    }

    // 글에 해당하는 전체 댓글 불러오기
    @Transactional(readOnly = true)
    public List<CommentDTO> getComments(int postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentDTO> commentDtos = new ArrayList<>();

        comments.forEach(s -> commentDtos.add(CommentDTO.toDto(s)));
        return commentDtos;
    }


    // 댓글 삭제하기
    @Transactional
    public String deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> {
            return new IllegalArgumentException("댓글 Id를 찾을 수 없습니다.");
        });
        commentRepository.deleteById(commentId);
        return "삭제 완료";
    }

}
