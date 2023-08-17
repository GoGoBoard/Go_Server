package Go.board.service;

import Go.board.dto.ArticlePagingDTO;
import Go.board.dto.ArticleResponseDTO;
import Go.board.dto.ArticleSaveDTO;
import Go.board.entity.ArticleEntity;
import Go.board.entity.FileEntity;
import Go.board.entity.MemberEntity;
import Go.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    //private final FileService fileService;
    private final ArticleRepository articleRepository;

    public void save(ArticleSaveDTO articleSaveDTO, int memberId) throws IOException {
        MemberEntity memberEntity = memberService.findMemberByMemberId(memberId);//글 주인을 찾자
        ArticleEntity articleEntity = ArticleEntity.toArticleEntity(articleSaveDTO);//제목, 글 저장
        articleEntity.setMember(memberEntity);//member설정
        articleEntity.setWriteTime(LocalDateTime.now());//현재 서버 시간으로 작성시간 설정
        /*List<FileEntity> files = fileService.handleFile(articleSaveDTO.getFiles());//파일처리
        if (!files.isEmpty()) {
            fileService.saveFile(files, articleEntity);
        }*/
        articleRepository.save(articleEntity);
    }

    public Optional<List<ArticleResponseDTO>> findAll() {
        List<ArticleEntity> articleEntityList = articleRepository.findAll();
        List<ArticleResponseDTO> articleResponseDTOList = new ArrayList<>();
        for (ArticleEntity articleEntity : articleEntityList) {
            articleResponseDTOList.add(ArticleResponseDTO.toarticleResponseDTO(articleEntity));
        }
        return Optional.of(articleResponseDTOList);
    }

    public ArticleEntity findByPostId(int postId) {//게시글 상세조회
        Optional<ArticleEntity> articleEntity = articleRepository.findById(postId);
        if (articleEntity.isPresent())
            return articleEntity.get();
        else return null;
      /*  List<FileEntity> fileList = fileService.getFileList(postId);
        List<String> filePathList = fileService.showFile(fileList);

            ArticleEntity articleEntity = optionalArticleEntity.get();
            ArticleResponseDTO articleResponseDTO = ArticleResponseDTO.toarticleResponseDTO(articleEntity);
            articleResponseDTO.setFilePathList(filePathList);*/

    }

    public ArticleResponseDTO showArticle(int postId) {
        //글,,
        ArticleEntity article = findByPostId(postId);
        if (article == null) return null; //해당 postId를 가진 글이 없을 경우

        //있으면 파일까지 다 같이 넣어서 보내자
        ArticleResponseDTO articleResponseDTO = ArticleResponseDTO.toarticleResponseDTO(article);
        return null;
    }

    public void update(int postId, ArticleSaveDTO articleSaveDTO) throws IOException {
        //글을 찾자
        ArticleEntity findArticle = articleRepository.findByPostId(postId);
        findArticle.setTitle(articleSaveDTO.getTitle());
        findArticle.setContent(articleSaveDTO.getContent());//제목,내용 수정

        List<MultipartFile> newfiles = articleSaveDTO.getFiles();
        List<FileEntity> oldfiles = findArticle.getFiles();
        //기존 파일과 신규 파일 구분해서 다시 저장
        //  fileService.updateFile(oldfiles, newfiles);
    }

    public boolean delete(int postId, int memberId) {
        ArticleEntity findArticle = findByPostId(postId);
        MemberEntity findMember = findArticle.getMember();
        if (findMember.getMemberId() != memberId) {
            return false;
        } else {
            articleRepository.deleteById(postId);
            return true;
        }
    }

    public Page<ArticlePagingDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;//페이지 위치에 있는 값은 0부터 시작 like배열
        int pageLimit = 3; //한 페이지에 보여줄 개수
        //한 페이지 당 3개씩 글을 보여주고 정렬기준은 id기준으로 내림차순 정렬
        Page<ArticleEntity> articleEntities =
                articleRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "postId")));
        Page<ArticlePagingDTO> articleDTOS =
                articleEntities.map(articleEntity -> ArticlePagingDTO.toarticlePagingDTO(articleEntity
                ));
        return articleDTOS;
    }
}
