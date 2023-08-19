package Go.board.controller;

import Go.board.dto.ArticlePagingDTO;
import Go.board.dto.ArticleResponseDTO;
import Go.board.dto.ArticleSaveDTO;
import Go.board.dto.CommentResponseDTO;
import Go.board.entity.ArticleEntity;
import Go.board.service.ArticleService;
import Go.board.service.CommentService;
import Go.board.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final FileService fileService;

    @PostMapping("")
    public ResponseEntity<String> save(
            @ModelAttribute ArticleSaveDTO articleSaveDTO
                                       ,HttpSession session
    ) {
        try {
            int memberId = (int) session.getAttribute("memberId");
            articleService.save(articleSaveDTO, memberId);
            return ResponseEntity.ok("저장 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("저장 실패");
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ArticleResponseDTO> getPostByPostId(@PathVariable("postId") int postId) {
        ArticleResponseDTO dto = articleService.GetArticle(postId);
        List<CommentResponseDTO> allComment = commentService.getAllComment(postId);
        dto.setComments(allComment);//댓글까지
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping("/{postId}")
    public ResponseEntity<String> update(@PathVariable int postId
            ,@ModelAttribute ArticleSaveDTO articleSaveDTO
    ) {
        try {
            //글 수정
            ArticleEntity update = articleService.update(postId, articleSaveDTO);
            //파일 수정
            fileService.updateFile(update, articleSaveDTO.getFiles());
            return ResponseEntity.ok("수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("저장 실패");
        }

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> delete(@PathVariable("postId") int postId, HttpSession session) {
        int memberId = (int) session.getAttribute("memberId");//유저찾
        boolean deleted = articleService.delete(postId, memberId);
        return deleted ? ResponseEntity.ok("삭제 성공") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("글 작성자만 삭제 가능");
    }

    @GetMapping("/paging")//article/paging?page=1
    public ResponseEntity<Page<ArticlePagingDTO>> paging(@PageableDefault(page = 1) Pageable pageable) {
        Page<ArticlePagingDTO> paging = articleService.paging(pageable);
        return ResponseEntity.ok(paging);
    }
}
