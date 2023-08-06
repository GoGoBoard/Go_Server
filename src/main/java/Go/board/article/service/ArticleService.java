package Go.board.article.service;

import Go.board.article.dto.ArticleDTO;
import Go.board.article.entitiy.ArticleEntity;
import Go.board.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//dto->entity(Entity class에서 진행),entity->dto(dto class에서 진행)작업 필요
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public void save(ArticleDTO articleDTO) {
        ArticleEntity articleEntity = ArticleEntity.toSaveEntity(articleDTO);
        articleRepository.save(articleEntity);
    }

    public Optional<List<ArticleDTO>> findAll() {
        List<ArticleEntity> articleEntityList = articleRepository.findAll();
        List<ArticleDTO> articleDTOList = new ArrayList<>();
        for (ArticleEntity articleEntity : articleEntityList) {
            articleDTOList.add(ArticleDTO.toarticleDTO(articleEntity));
        }
        return Optional.of(articleDTOList);
    }

    public ArticleDTO findByPostId(int postId) {
        Optional<ArticleEntity> optionalArticleEntity = articleRepository.findById(postId);
        if (optionalArticleEntity.isPresent()) {
            ArticleEntity articleEntity = optionalArticleEntity.get();
            ArticleDTO articleDTO = ArticleDTO.toarticleDTO(articleEntity);
            return articleDTO;
        } else return null;
    }

    public boolean delete(int postId) {
        if (articleRepository.existsById(postId)) {
            articleRepository.deleteById(postId);
            return true;
        }
        return false;
    }

    public Page<ArticleDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;//페이지 위치에 있는 값은 0부터 시작 like배열
        int pageLimit = 3; //한 페이지에 보여줄 개수
        //한 페이지 당 3개씩 글을 보여주고 정렬기준은 id기준으로 내림차순 정렬
        Page<ArticleEntity> articleEntities =
                articleRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "postId")));
        Page<ArticleDTO> articleDTOS =
                articleEntities.map(articleEntity -> ArticleDTO.toarticleDTO(articleEntity
                        ));
        return articleDTOS;
    }
}
