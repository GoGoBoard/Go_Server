package Go.board.controller;

import Go.board.dto.CommentResponse;
import Go.board.dto.CommentCreateRequest;
import Go.board.dto.UpdateCommentRequest;
import Go.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}") // 정상 작동
    public ResponseEntity<CommentResponse> createComment(HttpServletRequest request, @PathVariable int postId, @RequestBody CommentCreateRequest dto) {
        CommentResponse createdComment = commentService.createComment(request, postId, dto);
        if (createdComment != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{postId}") // 정상 작동
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable int postId) {
        List<CommentResponse> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{commentId}") // 정상 작동
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequest request) {
        CommentResponse updatedComment = commentService.updateComment(commentId, request.getContent());
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{commentId}") // 정상 작동
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        boolean deleted = commentService.deleteComment(commentId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}

