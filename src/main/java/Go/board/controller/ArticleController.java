package Go.board.controller;

import Go.board.dto.ArticlePagingDTO;
import Go.board.dto.ArticleRequestDTO;
import Go.board.dto.ArticleResponseDTO;
import Go.board.dto.CommentResponseDTO;
import Go.board.entity.ArticleEntity;
import Go.board.service.ArticleService;
import Go.board.service.CommentService;
import Go.board.service.FileService;
import Go.board.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final FileService fileService;
    private final RecommendService recommendService;

    @PostMapping("")
    public ResponseEntity save(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            HttpServletRequest request
    ) {
        try {
            //todo XSS
            HttpSession session = request.getSession(false);
            int memberId = (int) session.getAttribute("memberId");
            ArticleRequestDTO article = new ArticleRequestDTO(title, content, files);
            articleService.save(article, memberId);
            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ArticleResponseDTO> getPostByPostId(@PathVariable("postId") int postId) {
        ArticleResponseDTO dto = articleService.GetArticle(postId);
        if (dto == null) return ResponseEntity.noContent().build();
        List<CommentResponseDTO> allComment = commentService.getAllComment(postId);
        dto.setComments(allComment);//댓글까지
        //추천/비추천 개수
        int like = recommendService.getLike(postId);
        int dislike = recommendService.getDislike(postId);
        dto.setLike(like);
        dto.setDislike(dislike);
        return ResponseEntity.ok(dto);
    }

    @Transactional
    @PutMapping("/{postId}")
    public ResponseEntity update(@PathVariable int postId
            , @ModelAttribute ArticleRequestDTO articleRequestDTO,
                                 HttpServletRequest request
    ) {
        try {
            HttpSession session = request.getSession(false);
            int memberId = (int) session.getAttribute("memberId");
            ArticleEntity findArticle = articleService.findByPostId(postId);
            if (findArticle.getMember().getMemberId() != memberId)
                return ResponseEntity.badRequest().build();//글 작성자 아니면 수정 불가
            //글 수정
            ArticleEntity update = articleService.update(postId, articleRequestDTO);
            //파일 수정
            fileService.updateFile(update, articleRequestDTO.getFiles());
            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity delete(@PathVariable("postId") int postId, HttpServletRequest request) {
        //세션 유효성 확인
        HttpSession session = request.getSession(false);
        int memberId = (int) session.getAttribute("memberId");//유저찾
        boolean checkOwner = articleService.checkWriter(memberId, postId);
        if (!checkOwner) return ResponseEntity.status(401).build();//작성자 이외의 사람이 지울려하는 경우

        boolean deleted = articleService.delete(postId, memberId);
        return deleted ? ResponseEntity.ok("{}") : ResponseEntity.badRequest().build();//400
    }

    @GetMapping("/paging")//article/paging?page=1
    public ResponseEntity<Map<String, List<ArticlePagingDTO>>> paging(@PageableDefault(page = 1) Pageable pageable) {
        List<ArticlePagingDTO> paging = articleService.paging(pageable);
        for (ArticlePagingDTO dto : paging) {
            int like = recommendService.getLike(dto.getPostId());
            dto.setRecommend(like);
        }

        if (paging.size() != 0) {
            Map<String, List<ArticlePagingDTO>> result = new HashMap<>();
            result.put("content", paging);
            return ResponseEntity.ok(result);
        } else return ResponseEntity.badRequest().build();//400
    }

    @Transactional
    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Integer>> like(@PathVariable("postId") int postId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        int memberId = (int) session.getAttribute("memberId");
        recommendService.like(memberId, postId);
        Map<String, Integer> result = new HashMap<>();
        result.put("postId", postId);
        //게시글 추천
        return ResponseEntity.ok(result);
    }

    @Transactional
    @PostMapping("/{postId}/dislike")
    public ResponseEntity<Map<String, Integer>> dislike(@PathVariable("postId") int postId, HttpServletRequest request) {
        //게시글 비추천
        HttpSession session = request.getSession(false);
        int memberId = (int) session.getAttribute("memberId");
        recommendService.dislike(memberId, postId);
        Map<String, Integer> result = new HashMap<>();
        result.put("postId", postId);
        return ResponseEntity.ok(result);
    }


}
