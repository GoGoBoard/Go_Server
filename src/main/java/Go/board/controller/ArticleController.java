package Go.board.controller;

import Go.board.dto.ArticlePagingDTO;
import Go.board.dto.ArticleResponseDTO;
import Go.board.dto.ArticleSaveDTO;
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
            @RequestPart("file") List<MultipartFile> files,
            HttpServletRequest request
    ) {
        try {
            //todo XSS
            HttpSession session = request.getSession(false);
            int memberId = (int) session.getAttribute("memberId");
            ArticleSaveDTO article = new ArticleSaveDTO(title, content, files);
            articleService.save(article, memberId);
            return ResponseEntity.ok("저장 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ArticleResponseDTO> getPostByPostId(@PathVariable("postId") int postId) {
        ArticleResponseDTO dto = articleService.GetArticle(postId);
        if(dto==null) return ResponseEntity.noContent().build();
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
            , @ModelAttribute ArticleSaveDTO articleSaveDTO,
                                         HttpServletRequest request
    ) {
        try {
            HttpSession session = request.getSession(false);
            int memberId = (int) session.getAttribute("memberId");
            ArticleEntity findArticle = articleService.findByPostId(postId);
            if (findArticle.getMember().getMemberId() != memberId)
                return ResponseEntity.badRequest().build();//글 작성자 아니면 수정 불가
            //글 수정
            ArticleEntity update = articleService.update(postId, articleSaveDTO);
            //파일 수정
            fileService.updateFile(update, articleSaveDTO.getFiles());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity delete(@PathVariable("postId") int postId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        int memberId = (int) session.getAttribute("memberId");//유저찾
        boolean checkOwner = articleService.checkWriter(memberId, postId);
        if (!checkOwner) return ResponseEntity.status(401).build();//작성자 이외의 사람이 지울려하는 경우

        boolean deleted = articleService.delete(postId, memberId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();//400
    }

    @GetMapping("/paging")//article/paging?page=1
    public ResponseEntity<List<ArticlePagingDTO>> paging(@PageableDefault(page = 1) Pageable pageable) {
        List<ArticlePagingDTO> paging = articleService.paging(pageable);
        if (paging != null)
            return ResponseEntity.ok(paging);
        else return ResponseEntity.badRequest().build();//400
    }

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
