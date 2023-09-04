package Go.board.controller;

import Go.board.dto.CommentRequestDTO;
import Go.board.dto.CommentResponseDTO;
import Go.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getAllComment(@PathVariable int postId) {
        List<CommentResponseDTO> allComment = commentService.getAllComment(postId);
        return allComment != null ? ResponseEntity.ok(allComment) : ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDTO> saveComment(@PathVariable int postId, @RequestBody CommentRequestDTO commentRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        int memberId = (int) session.getAttribute("memberId");//댓글 작성자
        CommentResponseDTO dto = commentService.saveComment(commentRequest.getContent(), postId, memberId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.badRequest().build();
    }

    @Transactional // 따로 save하지 않아도 db수정됨(더티체킹)
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable int commentId, @RequestBody CommentRequestDTO commentRequest,
                                                            HttpServletRequest request) {

        try {
            HttpSession session = request.getSession(false);
            int memberId = (int) session.getAttribute("memberId");

            int findMember = commentService.FindMemberIdByCommentId(commentId);
            if (findMember != memberId) return ResponseEntity.badRequest().build();//작성자만 수정 가능
            CommentResponseDTO responseDTO = commentService.updateComment(commentId, commentRequest.getContent());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable int commentId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        int memberId = (int) session.getAttribute("memberId");
        boolean deleted = commentService.deleteComment(commentId, memberId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();

    }

}
