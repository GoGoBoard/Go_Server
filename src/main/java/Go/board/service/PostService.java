package Go.board.service;

import Go.board.dto.PostDTO;
import Go.board.entity.Post;
import Go.board.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostService(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    public List<PostDTO> getAllPostsByPage(int page) {
        int pageSize = 10;
        Page<Post> postPage = postRepository.findAll(PageRequest.of(page - 1, pageSize));
        return postPage.getContent().stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public PostDTO createPost(PostDTO newPostDTO) {
        Post newPost = modelMapper.map(newPostDTO, Post.class);
        Post createdPost = postRepository.save(newPost);
        return modelMapper.map(createdPost, PostDTO.class);
    }

    public Optional<PostDTO> getPostById(int postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.map(post -> modelMapper.map(post, PostDTO.class));
    }

    public boolean deletePostById(int postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        }
        return false;
    }

    public PostDTO updatePost(int postId, PostDTO updatedPostDTO) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            modelMapper.map(updatedPostDTO, existingPost);
            Post updatedPost = postRepository.save(existingPost);
            return modelMapper.map(updatedPost, PostDTO.class);
        }
        return null;
    }

}
