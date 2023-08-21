package Go.board.controller;

import Go.board.dto.PostDTO;
import Go.board.dto.PostSaveRequestDTO;
import Go.board.dto.PostSaveResponseDTO;
import Go.board.entity.Post;
import Go.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping() // 비정상 작동
    public ResponseEntity<List<PostDTO>> getAllPosts(@RequestParam(defaultValue = "1") int p) {
        List<PostDTO> posts = postService.getAllPostsByPage(p);
        return ResponseEntity.ok(posts);
    }

    @PostMapping()
    public ResponseEntity<PostSaveResponseDTO> createPost(HttpServletRequest request, @RequestBody PostSaveRequestDTO newPostDTO) {
        Post createdPost = postService.createPost(request,newPostDTO);
        PostSaveResponseDTO dto = new PostSaveResponseDTO();
        dto.setTitle(createdPost.getTitle());
        dto.setContent(createdPost.getContent());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{articleId}") // 비정상 작동
    public ResponseEntity<PostDTO> getPostById(@PathVariable int articleId) {
        Optional<PostDTO> optionalPost = postService.getPostById(articleId);
        return optionalPost.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deletePost(@PathVariable int articleId) {
        boolean deleted = postService.deletePostById(articleId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable int articleId, @RequestBody PostDTO updatedPostDTO) {
        PostDTO postDTO = postService.updatePost(articleId, updatedPostDTO);
        return postDTO != null ? ResponseEntity.ok(postDTO) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{articleId}")
    public ResponseEntity<PostDTO> partialUpdatePost(@PathVariable int articleId, @RequestBody PostDTO updatedFieldsDTO) {
        PostDTO postDTO = postService.partialUpdatePost(articleId, updatedFieldsDTO);
        return postDTO != null ? ResponseEntity.ok(postDTO) : ResponseEntity.notFound().build();
    }
}
