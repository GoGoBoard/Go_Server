package Go.board.dto;

import Go.board.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostOneResponse {

    private int postId;
    private String nickname;
    private String title;
    private String content;
    private String writeTime;
    private List<CommentResponse> comments = new ArrayList<>();

    public static PostOneResponse toDTO(Post post) {
        PostOneResponse postOneResponse = new PostOneResponse();
        postOneResponse.setPostId(post.getPostId());
        postOneResponse.setNickname(post.getMemberId().getNickname());
        postOneResponse.setTitle(post.getTitle());
        postOneResponse.setContent(post.getContent());
        postOneResponse.setWriteTime(post.getWriteTime());
        return postOneResponse;
    }
}
