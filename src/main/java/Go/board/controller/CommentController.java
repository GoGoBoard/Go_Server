package Go.board.controller;

import Go.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comment")
    public ResponseEntity saveComment(@PathVariable int postId, @RequestBody String content) {
        //  int memberId = (int) session.getAttribute("memberId");//댓글 작성자
        boolean saved = commentService.saveComment(content, postId, 41);
        return saved ? ResponseEntity.ok("댓글 저장 성공") : ResponseEntity.badRequest().build();
    }


}
