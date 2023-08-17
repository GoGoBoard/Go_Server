package Go.board.controller;

import Go.board.dto.ArticlePagingDTO;
import Go.board.dto.ArticleResponseDTO;
import Go.board.dto.ArticleSaveDTO;
import Go.board.entity.ArticleEntity;
import Go.board.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("")
    public ResponseEntity<List<ArticleResponseDTO>> getAll() {
        return articleService.findAll().map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("")
    public ResponseEntity<String> save(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "content") String content,
                                       @RequestPart(name = "files") List<MultipartFile> files,
                                       HttpSession session
    ) {//todo file controller article controller분리
        try {
            ArticleSaveDTO articleSaveDTO = new ArticleSaveDTO();
            articleSaveDTO.setTitle(title);
            articleSaveDTO.setContent(content);
            articleSaveDTO.setFiles(files);
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
        //Todo 첨부파일도 가져오도록,댓글도
        ArticleEntity article = articleService.findByPostId(postId);
       return null;
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> update(ArticleSaveDTO articleSaveDTO) {
        try {
            //Todo
            // articleService.save(articleResponseDTO);
            return ResponseEntity.ok("");
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
