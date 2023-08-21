package Go.board.service;

import Go.board.dto.CommentResponse;
import Go.board.dto.CommentCreateRequest;
import Go.board.entity.Comment;
import Go.board.entity.Member;
import Go.board.entity.Post;
import Go.board.repository.CommentRepository;
import Go.board.repository.MemberRepository;
import Go.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;


    // 댓글 생성
    public CommentResponse createComment(HttpServletRequest request, int postId, CommentCreateRequest dto) {

        // 댓글 입력하는 사용자 세션 정보 가져오기
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberRepository.findById(memberId).orElse(null);

        // postId 이용해서 Post 객체 받아오기
        Post post = postRepository.findById(postId).orElse(null);

        Comment comment = new Comment();
        comment.setPostId(post);
        comment.setMember_id(member);
        comment.setContent(dto.getContent());

        // Get the current date and time
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        comment.setWrite_time(formattedTime);

        Comment createdComment = commentRepository.save(comment);
        return CommentResponse.toDto(createdComment);
    }

    // 글에 해당하는 전체 댓글 불러오기
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(int postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<Comment> comments = commentRepository.findAllByPostId(post);
        return comments.stream()
                .map(CommentResponse::toDto)
                .collect(Collectors.toList());
    }

    // 댓글 수정
    public CommentResponse updateComment(Long commentId, String updatedContent) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(updatedContent);

            Comment updatedComment = commentRepository.save(comment);
            return CommentResponse.toDto(updatedComment);
        }
        return null; // Comment not found
    }


    // 댓글 삭제
    public boolean deleteComment(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return true;
        }
        return false; // Comment not found
    }

    public List<CommentResponse> getAllComment(int postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<Comment> all = commentRepository.findAllByPostId(post);
        List<CommentResponse> dtoList = new ArrayList<>(all.size());
        for (Comment comment : all) {
            CommentResponse dto = CommentResponse.toDto(comment);
            dtoList.add(dto);
        }
        return dtoList;
    }
}

