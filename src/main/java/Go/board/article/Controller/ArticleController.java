package Go.board.article.Controller;

import Go.board.article.dto.ArticleDTO;
import Go.board.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("")
    public ResponseEntity<List<ArticleDTO>> getAll() {
        return articleService.findAll().map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody ArticleDTO articleDTO) {
        try {
            articleService.save(articleDTO);
            return ResponseEntity.ok("저장 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 실패");
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ArticleDTO> getPostByPostId(@PathVariable("postId") int postId) {
        ArticleDTO articleDTO = articleService.findByPostId(postId);
        return articleDTO != null ? ResponseEntity.ok(articleDTO) : ResponseEntity.notFound().build();
        //return ResponseEntity.ok(articleDTO);

    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> update(ArticleDTO articleDTO) {
        try {
            articleService.save(articleDTO);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 실패");
        }

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity delete(@PathVariable("postId") int postId) {
        boolean deleted = articleService.delete(postId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/paging")//article/paging?page=1
    public ResponseEntity<Page<ArticleDTO>> paging(@PageableDefault(page = 1) Pageable pageable) {
        // pageable.getPageNumber();
        Page<ArticleDTO> articleList = articleService.paging(pageable);
        return ResponseEntity.ok(articleList);
    }
}
