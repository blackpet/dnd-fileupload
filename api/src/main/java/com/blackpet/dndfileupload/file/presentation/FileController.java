package com.blackpet.dndfileupload.file.presentation;

import com.blackpet.dndfileupload.file.domain.AttachFile;
import com.blackpet.dndfileupload.file.infrastructure.FileRepository;
import com.blackpet.dndfileupload.file.infrastructure.dto.AttachFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

  @Value("${upload.directory}")
  private String uploadDirectory;

  private final FileRepository fileRepository;

  @PostMapping("/files/upload")
  public ResponseEntity<AttachFileResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

    Path targetDirectory = Paths.get(uploadDirectory);

    AttachFile attachFile = new AttachFile(file.getOriginalFilename(), file.getContentType(), file.getSize(), targetDirectory);

    // copy physical file
    copyFileToPath(file.getInputStream(), attachFile);

    // save to DB
    fileRepository.save(attachFile);

    AttachFileResponse response = AttachFileResponse.builder()
            .id(attachFile.getId())
            .name(attachFile.getName())
            .build();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/files/download/{id}")
  public void downloadFile(@PathVariable UUID id, HttpServletResponse response) throws IOException {
    AttachFile file = fileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("첨부되지 않은 파일입니다."));

    file.verifyDownload();

    InputStream inputStream = new FileInputStream(file.getFilePath().toFile());

    response.setContentType(file.getContentType());
    response.setHeader("Content-disposition", "attachment; filename="
            + new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
    IOUtils.copy(inputStream, response.getOutputStream());
  }

  @DeleteMapping("/files/{id}")
  public ResponseEntity<Boolean> deleteFile(@PathVariable UUID id) throws IOException {
    AttachFile file = fileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 파일입니다."));

    fileRepository.delete(file);
    Files.delete(file.getFilePath());

    return ResponseEntity.ok(true);
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

  @GetMapping("/hello")
  public String hello() {
    return "hello~~~u~~~~~";
  }
}
