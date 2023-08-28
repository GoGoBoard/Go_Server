package Go.board.controller;

import Go.board.dto.*;
import Go.board.entity.Post;
import Go.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/paging")//article/paging?page=1
    public ResponseEntity<ArticlePagingResponse> paging(@PageableDefault(page = 1) Pageable pageable) {
        ArticlePagingResponse pagingResponse = postService.paging(pageable);
        return ResponseEntity.ok(pagingResponse);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostSaveResponseDTO> createPost(
            HttpServletRequest request,
            @RequestPart(value = "newPost") PostSaveRequestDTO newPostDTO,
            @RequestPart(value = "image", required=false) List<MultipartFile> files
    )throws Exception {
        Post createdPost = postService.createPost(request,newPostDTO,files);
        PostSaveResponseDTO dto = new PostSaveResponseDTO();
        dto.setTitle(createdPost.getTitle());
        dto.setContent(createdPost.getContent());
//        dto.setFilePathList(createdPost.getAttachment());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{articleId}") // 정상 작동
    public ResponseEntity<PostOneResponse> getPostById(@PathVariable int articleId) {
        PostOneResponse dto = postService.getPostById(articleId);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deletePost(@PathVariable int articleId) {
        boolean deleted = postService.deletePostById(articleId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<PostAllResponse> updatePost(@PathVariable int articleId, @RequestBody PostAllResponse updatedPostAllResponse) {
        PostAllResponse postAllResponse = postService.updatePost(articleId, updatedPostAllResponse);
        return postAllResponse != null ? ResponseEntity.ok(postAllResponse) : ResponseEntity.notFound().build();
    }

//    @PatchMapping("/{articleId}")
//    public ResponseEntity<PostAllResponse> partialUpdatePost(@PathVariable int articleId, @RequestBody PostAllResponse updatedFieldsDTO) {
//        PostAllResponse postAllResponse = postService.partialUpdatePost(articleId, updatedFieldsDTO);
//        return postAllResponse != null ? ResponseEntity.ok(postAllResponse) : ResponseEntity.notFound().build();
//    }
}
