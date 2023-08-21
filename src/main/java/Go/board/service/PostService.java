package Go.board.service;

import Go.board.dto.CommentResponse;
import Go.board.dto.PostAllResponse;
import Go.board.dto.PostOneResponse;
import Go.board.dto.PostSaveRequestDTO;
import Go.board.entity.Comment;
import Go.board.entity.Member;
import Go.board.entity.Post;
import Go.board.repository.CommentRepository;
import Go.board.repository.MemberRepository;
import Go.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final CommentService commentService;

    public List<PostAllResponse> getAllPostsByPage(int page) {
        int pageSize = 10;
        Page<Post> postPage = postRepository.findAll(PageRequest.of(page - 1, pageSize));
        return postPage.getContent().stream()
                .map(post -> modelMapper.map(post, PostAllResponse.class))
                .collect(Collectors.toList());    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(HttpServletRequest request, PostSaveRequestDTO newPostDTO) {
        // 글을 작성하려는 작성자의 세션 정보 얻어오기
        HttpSession session = request.getSession();
        Long memberId = (Long)session.getAttribute("memberId");
        Member member = memberRepository.findById(memberId).orElse(null);

        Post newPost = new Post();
        newPost.setMemberId(member);
        newPost.setTitle(newPostDTO.getTitle());
        newPost.setContent(newPostDTO.getContent());

        // 작성 시간 설정
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        newPost.setWriteTime(formattedTime);

        Post createdPost = postRepository.save(newPost);
        return createdPost;

    }

    public PostOneResponse getPostById(int postId) {
        Optional<Post> optionalPost = postRepository.findById(postId); // 게시글 정보 가져오기

        List<CommentResponse> comments = commentService.getAllComment(postId); // 해당 게시글 댓글들 가져오기

        if (optionalPost.isPresent()) {
            Post getPost = optionalPost.get();
            PostOneResponse dto = PostOneResponse.toDTO(getPost);
            dto.setComments(comments);
            return dto;
        } else return null;
    }

    public boolean deletePostById(int postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        }
        return false;
    }

    public PostAllResponse updatePost(int postId, PostAllResponse updatedPostAllResponse) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            modelMapper.map(updatedPostAllResponse, existingPost);
            Post updatedPost = postRepository.save(existingPost);
            return modelMapper.map(updatedPost, PostAllResponse.class);
        }
        return null;
    }

    public PostAllResponse partialUpdatePost(int postId, PostAllResponse updatedFieldsDTO) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (updatedFieldsDTO.getTitle() != null) {
                existingPost.setTitle(updatedFieldsDTO.getTitle());
            }
            if (updatedFieldsDTO.getContent() != null) {
                existingPost.setContent(updatedFieldsDTO.getContent());
            }
            if (updatedFieldsDTO.getWriteTime() != null) {
                existingPost.setWriteTime(updatedFieldsDTO.getWriteTime());
            }
            // Save the updated post
            Post updatedPost = postRepository.save(existingPost);
            return modelMapper.map(updatedPost, PostAllResponse.class); // Map entity to DTO
        }
        return null;
    }

}
