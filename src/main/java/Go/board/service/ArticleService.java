package Go.board.service;

import Go.board.dto.ArticlePagingDTO;
import Go.board.dto.ArticleResponseDTO;
import Go.board.dto.ArticleSaveDTO;
import Go.board.entity.ArticleEntity;
import Go.board.entity.CommentEntity;
import Go.board.entity.FileEntity;
import Go.board.entity.MemberEntity;
import Go.board.repository.ArticleRepository;
import Go.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final CommentRepository commentRepository;

    public void save(ArticleSaveDTO articleSaveDTO, int memberId) {//저장
        MemberEntity memberEntity = memberService.findMemberByMemberId(memberId);//글 주인을 찾자
        ArticleEntity articleEntity = ArticleEntity.toArticleEntity(articleSaveDTO);//제목, 글 저장
        articleEntity.setMember(memberEntity);//member설정
        articleEntity.setWriteTime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());//현재 서버 시간으로 작성시간 설정
        List<FileEntity> files = fileService.handleFile(articleSaveDTO.getFiles());//파일처리
        if (!files.isEmpty()) {
            fileService.saveFile(files, articleEntity);
        }
        articleRepository.save(articleEntity);
    }

    public ArticleResponseDTO GetArticle(int postId) {//게시글 상세조회
        Optional<ArticleEntity> articleEntity = articleRepository.findById(postId);
        if (articleEntity.isPresent()) {
            ArticleEntity article = articleEntity.get();//엔티티
            List<String> filePathList = fileService.getFilePathList(article);//파일
            ArticleResponseDTO dto = ArticleResponseDTO.toarticleResponseDTO(article);
            dto.setFilePathList(filePathList);
            return dto;
        } else return null;
    }

    public ArticleEntity findByPostId(int postId) {
        Optional<ArticleEntity> articleEntity = articleRepository.findById(postId);
        return articleEntity.orElse(null);
    }

    public ArticleEntity update(int postId, ArticleSaveDTO articleSaveDTO) {
        //글을 찾자
        ArticleEntity findArticle = articleRepository.findByPostId(postId);
        findArticle.setTitle(articleSaveDTO.getTitle());
        findArticle.setContent(articleSaveDTO.getContent());//제목,내용 수정
        return findArticle;
    }

    public boolean delete(int postId, int memberId) {
        ArticleEntity findArticle = findByPostId(postId);
        //댓글이 있다면 댓글 먼저 삭제하자
        List<CommentEntity> comments = commentRepository.findAllByPostId(findArticle);
        commentRepository.deleteAll(comments);
        MemberEntity findMember = findArticle.getMember();
        if (findMember.getMemberId() != memberId) {
            return false;
        } else {
            articleRepository.deleteById(postId);
            return true;
        }
    }

    public List<ArticlePagingDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;//페이지 위치에 있는 값은 0부터 시작 like배열
        int pageLimit = 3; //한 페이지에 보여줄 개수
        //한 페이지 당 3개씩 글을 보여주고 정렬기준은 id기준으로 내림차순 정렬
        Page<ArticleEntity> articleEntities =
                articleRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "postId")));
        List<ArticlePagingDTO> dto = new ArrayList<>();
        for (ArticleEntity articleEntity : articleEntities) {
            dto.add(ArticlePagingDTO.toarticlePagingDTO(articleEntity));
        }
        return dto;
    }

    public boolean checkWriter(int memberId, int postId) {
        //글 작성자와 접근하려고하는 사람 일치여부 확인
        MemberEntity findMember = memberService.findMemberByMemberId(memberId);
        ArticleEntity findArticle = findByPostId(postId);
        MemberEntity articleOwner = findArticle.getMember();
        return findMember.getMemberId() == articleOwner.getMemberId();
    }
}
