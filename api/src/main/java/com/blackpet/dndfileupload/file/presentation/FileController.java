package com.blackpet.dndfileupload.file.presentation;

import com.blackpet.dndfileupload.file.domain.AttachFile;
import com.blackpet.dndfileupload.file.infrastructure.FileRepository;
import com.blackpet.dndfileupload.file.infrastructure.dto.FileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

  @Value("${upload.directory}")
  private String uploadDirectory;

  private final FileRepository fileRepository;

  @PostMapping("/files/upload")
  public ResponseEntity<FileResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

    Path targetDirectory = Paths.get(uploadDirectory);

    AttachFile attachFile = new AttachFile(file.getOriginalFilename(), file.getContentType(), file.getSize(), targetDirectory);

    // copy physical file
    copyFileToPath(file.getInputStream(), attachFile);

    // save to DB
    fileRepository.save(attachFile);

    FileResponse response = FileResponse.builder()
            .id(attachFile.getId())
            .originFilename(attachFile.getOriginFilename())
            .build();

    return ResponseEntity.ok(response);
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
      throw new RuntimeException(String.format(("파일 업로드가 실패했습니다. (%s)"), file.getOriginFilename()));
    }
  }

  @GetMapping("/hello")
  public String hello() {
    return "hello~~~u~~~~~";
  }
}
