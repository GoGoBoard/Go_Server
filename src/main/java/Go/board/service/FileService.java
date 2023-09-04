package Go.board.service;

import Go.board.entity.ArticleEntity;
import Go.board.entity.FileEntity;
import Go.board.repository.FileRepository;
import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    //파일서비스에서는 받아온 파일의 확장자를 확인하고
    //로컬에 실제 파일 저장
    //db에 저장할 이름을 만들고  파일 엔티티 리스트를 반환한다
    public List<FileEntity> handleFile(List<MultipartFile> fileList) {
        String path = "/home/~/Go_Server/image";
        String privateKeyFilePath = "C://Users//오주은//Downloads//gogoboard_key (1).pem";
        String username = "ubuntu"; // Replace with your Ubuntu server username
        String host = "52.79.65.54"; // Replace with your Ubuntu server IP or hostname
        //서버실행할 때 환경변수 전해주는 식으로 -? 민감한 정보, 서버 재컴파일할 필요 사라짐

        List<FileEntity> fileEntityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileList)) {//첨부파일 존재한다면
            //파일들을 저장할 경로 설정
            // String path = "/home/~/Go_Server/image";
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
                //파일id나 uuid을 파일명으로
                FileEntity fileEntity = new FileEntity(
                        f.getOriginalFilename(),
                        path + "//" + newFileName
                );
                fileEntityList.add(fileEntity);
                file = new File(path + File.separator + newFileName);

                try {
                    JSch jsch = new JSch();
                    Session session = jsch.getSession(username, host);
                    session.setConfig("StrictHostKeyChecking", "no");

                    // Load your private key for authentication
                    jsch.addIdentity(privateKeyFilePath);

                    session.connect();

                    ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                    channelSftp.connect();

                    // Transfer the file from local to remote
                    channelSftp.put(new FileInputStream(file), path);

                    channelSftp.disconnect();
                    session.disconnect();
                } catch (JSchException | SftpException | IOException e) {
                    e.printStackTrace();
                }
                //f.transferTo(file);//로컬에 저장
                file.setWritable(true);
                file.setReadable(true);
            }
        }
        return fileEntityList;

    }

    public List<FileEntity> getFileList(ArticleEntity article) {
        List<FileEntity> files = fileRepository.findAllByArticle(article);
        return files;
    }

    public List<String> getFilePathList(ArticleEntity article) {
        List<FileEntity> fileList = getFileList(article);
        List<String> filePathList = new ArrayList<>();
        for (FileEntity fileEntity : fileList) {
            filePathList.add(fileEntity.getFileName());
        }
        return filePathList;
    }

    public void saveFile(List<FileEntity> files, ArticleEntity articleEntity) {
        for (FileEntity file : files) {
            file.setArticle(articleEntity);
            articleEntity.addFile(fileRepository.save(file));
        }
    }

    public void updateFile(ArticleEntity article, List<MultipartFile> files) {
        //삭제하고 새로 다시 넣자?
        fileRepository.deleteAllByArticle(article);
        List<FileEntity> fileEntityList = handleFile(files);
        if (fileEntityList != null)
            saveFile(fileEntityList, article);

    }
}
