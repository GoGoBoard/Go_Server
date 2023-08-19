package Go.board.controller;

import Go.board.dto.CommentResponseDTO;
import Go.board.entity.CommentEntity;
import Go.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity saveComment(@PathVariable int postId, @RequestBody String content, HttpSession session) {
        int memberId = (int) session.getAttribute("memberId");//댓글 작성자
        boolean saved = commentService.saveComment(content, postId, memberId);
        return saved ? ResponseEntity.ok("댓글 저장 성공") : ResponseEntity.badRequest().build();
    }

    @Transactional // 따로 save하지 않아도 db수정됨(더티체킹)
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable int commentId, @RequestBody String content) {

        try {
            CommentEntity find = commentService.FindByCommentId(commentId);
            CommentResponseDTO responseDTO = commentService.updateComment(find, content);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable int commentId) {
        //Todo Cannot delete or update a parent row: a foreign key constraint fails 에러
        boolean deleted = commentService.deleteComment(commentId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();

    }

}
