package Go.board.service;

import Go.board.entity.FileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    //파일서비스에서는 받아온 파일의 확장자를 확인하고
    //로컬에 실제 파일 저장
    //db에 저장할 이름을 만들고  파일 엔티티 리스트를 반환한다
    public List<FileEntity> handleFile(List<MultipartFile> fileList) throws IOException {
        List<FileEntity> fileEntityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileList)) {//첨부파일 존재한다면
            //파일들을 저장할 경로 설정
            String path = "C://Users//오주은//Desktop//personal study//GoBoardUploadFiles";
            File file = new File(path);
            if (!file.exists()) {
                boolean success = file.mkdirs();
                if (!success) {
                    System.out.println("파일 저장 디렉토리 생성 실패");
                }
            }
            //파일명을 db에 저장할 이름으로 변경
            for (MultipartFile f : fileList) {
                String fileExtension;//파일 확장자
                String contentType = f.getContentType();
                if (contentType == null) {
                    //확장자 없는 경우
                    continue;
                } else {
                    if (contentType.contains("image/jpeg"))
                        fileExtension = ".jpg";
                    else if (contentType.contains("image/png"))
                        fileExtension = ".png";
                    else {
                        continue;
                    }
                }
                //db에 저장될 파일 이름이 중복되지 않게하기 위해 나노초로
                String newFileName = System.nanoTime() + fileExtension;
                FileEntity fileEntity = new FileEntity(
                        f.getOriginalFilename(),
                        path + "//" + newFileName
                );
                fileEntityList.add(fileEntity);
                file = new File(path + File.separator + newFileName);
                f.transferTo(file);//로컬에 저장
                file.setWritable(true);
                file.setReadable(true);
            }
        }
        return fileEntityList;

    }
}
