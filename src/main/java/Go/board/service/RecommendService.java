package Go.board.service;

import Go.board.entity.Member;
import Go.board.entity.Post;
import Go.board.entity.Recommend;
import Go.board.repository.MemberRepository;
import Go.board.repository.PostRepository;
import Go.board.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Member like(int articleId, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberRepository.findById(memberId).orElse(null);

        Post post = postRepository.findById(articleId).orElse(null);

        // 해당 게시글에 대한 추천, 비추천 기록이 있는지 확인
        if(recommendRepository.findByMemberAndPost(member, post).isPresent()) {
            Recommend recommend = recommendRepository.findByMemberAndPost(member, post).orElse(null);
            // 이미 좋아요를 눌렀다면 좋아요를 취소
            if (recommend.isRecommend()) {
                recommendRepository.delete(recommend);
                return member;
            }
            // 싫어요를 눌렀던 상황이면 싫어요를 좋아요로 변경
            else {
                recommend.setRecommend(true);
                return member;
            }
        }

        Recommend recommend = new Recommend();
        recommend.setMember(member);
        recommend.setPost(post);
        recommend.setRecommend(true);

        recommendRepository.save(recommend);

        return member;
    }

    @Transactional
    public Member dislike(int articleId, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberRepository.findById(memberId).orElse(null);

        Post post = postRepository.findById(articleId).orElse(null);

        // 해당 게시글에 대한 추천, 비추천 기록이 있는지 확인
        if(recommendRepository.findByMemberAndPost(member, post).isPresent()) {
            Recommend recommend = recommendRepository.findByMemberAndPost(member, post).orElse(null);
            // 이미 비추천을 눌렀다면 비추천을 취소
            if (!recommend.isRecommend()) {
                recommendRepository.delete(recommend);
                return member;
            }
            // 좋아요를 눌렀던 상황이면 좋아요를 싫어요로 변경
            else {
                recommend.setRecommend(false);
                return member;
            }
        }

        Recommend recommend = new Recommend();
        recommend.setMember(member);
        recommend.setPost(post);
        recommend.setRecommend(false);

        recommendRepository.save(recommend);

        return member;
    }

    public int getLike(int postId) {
        // 좋아요 개수를 가져올 게시글 가져오기
        Post post = postRepository.findById(postId).orElse(null);
        List<Recommend> likePosts = recommendRepository.findByPostAndRecommend(post,true);
        return likePosts.size();
    }

    public int getDislike(int postId) {
        // 싫어요 개수를 가져올 게시글 가져오기
        Post post = postRepository.findById(postId).orElse(null);
        List<Recommend> likePosts = recommendRepository.findByPostAndRecommend(post,false);
        return likePosts.size();
    }

}
