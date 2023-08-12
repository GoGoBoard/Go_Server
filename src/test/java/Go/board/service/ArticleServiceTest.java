package Go.board.service;

import Go.board.dto.ArticleSaveDTO;
import Go.board.entity.MemberEntity;
import Go.board.repository.ArticleRepository;
import Go.board.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

class ArticleServiceTest {
    private ArticleRepository articleRepository;
    private FileRepository fileRepository;

    @Test
    void saveTest() {//글저장
        //given
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberId(0);
        memberEntity.setLoginId("jueun");
        memberEntity.setPassword("1234");

        ArticleSaveDTO articleSaveDTO = new ArticleSaveDTO();
        articleSaveDTO.setTitle("테스트입니다 제목");
        articleSaveDTO.setContent("글 저장 테스트입니다");

        List<MultipartFile> files = new ArrayList<>();


    }

}