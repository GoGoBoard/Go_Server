package Go.board.service;

import Go.board.entity.ArticleEntity;
import Go.board.entity.MemberEntity;
import Go.board.entity.RecommendEntity;
import Go.board.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RecommendRepository recommendRepository;

    private final MemberService memberService;
    private final ArticleService articleService;

    public void like(int memberId, int postId) {
        MemberEntity findMember = memberService.findMemberByMemberId(memberId);
        ArticleEntity findArticle = articleService.findByPostId(postId);
        Optional<RecommendEntity> entity = recommendRepository.findByMemberIdAndPostId(findMember, findArticle);
        if (entity.isEmpty()) {
            RecommendEntity recommendEntity = makeEntity(true, findMember, findArticle);
            recommendRepository.save(recommendEntity);
        } else {
            //이미 추천/비추천했을 경우
            entity.get().setRecommend(true);
        }

    }

    public void dislike(int memberId, int postId) {
        MemberEntity findMember = memberService.findMemberByMemberId(memberId);
        ArticleEntity findArticle = articleService.findByPostId(postId);
        Optional<RecommendEntity> entity = recommendRepository.findByMemberIdAndPostId(findMember, findArticle);
        if (entity.isEmpty()) {
            RecommendEntity recommendEntity = makeEntity(false, findMember, findArticle);
            recommendRepository.save(recommendEntity);
        } else {
            //이미 추천/비추천했을 경우
            entity.get().setRecommend(false);
        }
    }

    private static RecommendEntity makeEntity(boolean recommend, MemberEntity findMember, ArticleEntity findArticle) {
        RecommendEntity recommendEntity = new RecommendEntity();
        recommendEntity.setRecommend(recommend);
        recommendEntity.setPostId(findArticle);
        recommendEntity.setMemberId(findMember);
        return recommendEntity;
    }

    public int getLike(int postId) {
        ArticleEntity findArticle = articleService.findByPostId(postId);
        List<RecommendEntity> likeArticles = recommendRepository.findByPostIdAndRecommend(findArticle, true);
        return likeArticles.size();
    }

    public int getDislike(int postId) {
        ArticleEntity findArticle = articleService.findByPostId(postId);
        List<RecommendEntity> likeArticles = recommendRepository.findByPostIdAndRecommend(findArticle, false);
        return likeArticles.size();
    }

}
