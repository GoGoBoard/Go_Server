package Go.board.service;

import Go.board.entity.Post;
import Go.board.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPostsByPage(int page) {
        int pageSize = 10;
        Page<Post> postPage = postRepository.findAll(PageRequest.of(page - 1, pageSize));
        return postPage.getContent();
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(Post newPost) {
        return postRepository.save(newPost);
    }

    public Optional<Post> getPostById(int postId) {
        return postRepository.findById(postId);
    }

    public boolean deletePostById(int postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        }
        return false;
    }

    public Post updatePost(int postId, Post updatedPost) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // Update the fields of the existing post with data from the updatedPost
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setWriteTime(updatedPost.getWriteTime());
            // Save the updated post
            return postRepository.save(post);
        }
        return null;
    }

}
