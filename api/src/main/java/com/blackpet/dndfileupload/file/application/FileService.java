package com.blackpet.dndfileupload.file.application;

import com.blackpet.dndfileupload.file.domain.AttachFile;
import com.blackpet.dndfileupload.file.infrastructure.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
  @Value("${upload.directory}")
  private String uploadDirectory;

  private final FileRepository fileRepository;

  public AttachFile saveFile(MultipartFile file) throws IOException {
    Path targetDirectory = Paths.get(uploadDirectory);
    AttachFile attachFile = new AttachFile(file.getOriginalFilename(), file.getContentType(), file.getSize(), targetDirectory);

    // copy physical file
    copyFileToPath(file.getInputStream(), attachFile);

    // save to DB
    AttachFile saved = fileRepository.save(attachFile);

    return saved;
  }

  public AttachFile getAttachFile(UUID id) {
    AttachFile file = fileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("첨부되지 않은 파일입니다."));

    file.verifyDownload();

    return file;
  }

  public boolean deleteFile(UUID id) throws IOException {
    AttachFile file = fileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 파일입니다."));

    fileRepository.delete(file);
    Files.delete(file.getFilePath());

    return true;
  }

  private void copyFileToPath(InputStream inputStream, AttachFile file) {
    Path target = file.getFilePath();

    try (inputStream) {
      // create non exists directories
      if (Files.notExists(target.getParent())) {
        Files.createDirectories(target.getParent());
      }
      Files.copy(inputStream, target);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(String.format(("파일 업로드가 실패했습니다. (%s)"), file.getName()));
    }
  }

}
