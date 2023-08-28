package Go.board.service;

import Go.board.Handler.FileHandler;
import Go.board.dto.*;
import Go.board.entity.Attachment;
import Go.board.entity.Comment;
import Go.board.entity.Member;
import Go.board.entity.Post;
import Go.board.repository.AttachmentRepository;
import Go.board.repository.CommentRepository;
import Go.board.repository.MemberRepository;
import Go.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AttachmentRepository attachmentRepository;
    private final ModelMapper modelMapper;
    private final CommentService commentService;
    private final FileHandler fileHandler;

    public ArticlePagingResponse paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;//페이지 위치에 있는 값은 0부터 시작 like배열
        int pageLimit = 6; //한 페이지에 보여줄 개수
        //한 페이지 당 6개씩 글을 보여주고 정렬기준은 id기준으로 내림차순 정렬
        Page<Post> postEntities =
                postRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "postId")));
        List<ArticlePaging> articleDTOS = postEntities.getContent().stream()
                .map(ArticlePaging::toarticlePagingDTO)
                .collect(Collectors.toList());
        return new ArticlePagingResponse(articleDTOS);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(HttpServletRequest request,
                           PostSaveRequestDTO newPostDTO,
                           List<MultipartFile> files
    )throws Exception {
        // 글을 작성하려는 작성자의 세션 정보 얻어오기
        HttpSession session = request.getSession();
        Long memberId = (Long)session.getAttribute("memberId");
        Member member = memberRepository.findById(memberId).orElse(null);

        Post newPost = new Post();
        newPost.setMemberId(member);
        newPost.setTitle(newPostDTO.getTitle());
        newPost.setContent(newPostDTO.getContent());

        // 작성 시간 설정
        newPost.setWriteTime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        List<Attachment> attachmentList = fileHandler.parseFileInfo(files);

        // 파일이 존재할 때에만 처리
        if(!attachmentList.isEmpty()) {
            for(Attachment attachment : attachmentList) {
                // 파일을 DB에 저장
                newPost.addPhoto(attachmentRepository.save(attachment));
            }
        }

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

//    public PostAllResponse partialUpdatePost(int postId, PostAllResponse updatedFieldsDTO) {
//        Optional<Post> optionalPost = postRepository.findById(postId);
//        if (optionalPost.isPresent()) {
//            Post existingPost = optionalPost.get();
//            if (updatedFieldsDTO.getTitle() != null) {
//                existingPost.setTitle(updatedFieldsDTO.getTitle());
//            }
//            if (updatedFieldsDTO.getContent() != null) {
//                existingPost.setContent(updatedFieldsDTO.getContent());
//            }
//            if (updatedFieldsDTO.getWriteTime() != null) {
//                existingPost.setWriteTime(updatedFieldsDTO.getWriteTime());
//            }
//            // Save the updated post
//            Post updatedPost = postRepository.save(existingPost);
//            return modelMapper.map(updatedPost, PostAllResponse.class); // Map entity to DTO
//        }
//        return null;
//    }

}
