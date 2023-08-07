package Go.board.controller;

import Go.board.dto.PostDTO;
import Go.board.entity.Post;
import Go.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/article")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getAllPosts(@RequestParam(defaultValue = "1") int p) {
        List<PostDTO> posts = postService.getAllPostsByPage(p);
        return ResponseEntity.ok(posts);
    }

    @PostMapping()
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO newPostDTO) {
        PostDTO createdPost = postService.createPost(newPostDTO);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{articleId}")
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
}
