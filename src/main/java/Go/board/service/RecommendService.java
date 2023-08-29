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

        // 이미 좋아요 되어있으면 에러 반환
        if(recommendRepository.findByMemberAndBoard(member, post).isPresent()) {
            throw new Exception();
        }

        Recommend recommend = new Recommend();
        recommend.setMember(member);
        recommend.setPost(post);
        recommend.setRecommend(true);

        recommendRepository.save(recommend);

        return member;
    }

    @Transactional
    public Member notLike(int articleId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberRepository.findById(memberId).orElse(null);

        Post post = postRepository.findById(articleId).orElse(null);

        Recommend recommend = recommendRepository.findByMemberAndBoard(member, post).orElse(null);

        recommendRepository.delete(recommend);

        return member;
    }
}
