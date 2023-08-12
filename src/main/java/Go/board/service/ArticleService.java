package Go.board.service;

import Go.board.dto.ArticleDTO;
import Go.board.dto.ArticleSaveDTO;
import Go.board.entity.ArticleEntity;
import Go.board.entity.FileEntity;
import Go.board.entity.MemberEntity;
import Go.board.repository.ArticleRepository;
import Go.board.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//dto->entity(Entity class에서 진행),entity->dto(dto class에서 진행)작업 필요
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final MemberService memberService;
    private final FileService fileService;
    private final ArticleRepository articleRepository;
    private final FileRepository fileRepository;

    public void save(ArticleSaveDTO articleSaveDTO, int memberId) throws IOException {
        MemberEntity memberEntity = memberService.findMemberByMemberId(memberId);//글 주인을 찾자
        System.out.println(memberEntity);
        ArticleEntity articleEntity = ArticleEntity.toArticleEntity(articleSaveDTO);//제목, 글 저장
        articleEntity.setMember(memberEntity);//member설정
        articleEntity.setWriteTime(LocalDateTime.now());//현재 서버 시간으로 작성시간 설정

        List<FileEntity> files = fileService.handleFile(articleSaveDTO.getFiles());//파일처리
        if (!files.isEmpty()) {
            for (FileEntity file : files) {
                file.setArticle(articleEntity);
                articleEntity.addFile(fileRepository.save(file));
            }
        }
        System.out.println(articleEntity);
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
