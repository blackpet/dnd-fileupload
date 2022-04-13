package com.blackpet.dndfileupload.file.presentation;

import com.blackpet.dndfileupload.file.application.FileService;
import com.blackpet.dndfileupload.file.domain.AttachFile;
import com.blackpet.dndfileupload.file.infrastructure.FileRepository;
import com.blackpet.dndfileupload.file.infrastructure.dto.AttachFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

  private final FileService fileService;

  @PostMapping("/files/upload")
  public ResponseEntity<AttachFileResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    AttachFile attachFile = fileService.saveFile(file);

    AttachFileResponse response = AttachFileResponse.builder()
            .id(attachFile.getId())
            .name(attachFile.getName())
            .build();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/files/download/{id}")
  public void downloadFile(@PathVariable UUID id, HttpServletResponse response) throws IOException {
    AttachFile file = fileService.getAttachFile(id);

    InputStream inputStream = new FileInputStream(file.getFilePath().toFile());

    response.setContentType(file.getContentType());
    response.setHeader("Content-disposition", "attachment; filename="
            + new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
    IOUtils.copy(inputStream, response.getOutputStream());
  }

  @DeleteMapping("/files/{id}")
  public ResponseEntity<Boolean> deleteFile(@PathVariable UUID id) throws IOException {
    boolean res = fileService.deleteFile(id);

    return ResponseEntity.ok(res);
  }


  @GetMapping("/hello")
  public String hello() {
    return "hello~~~u~~~~~";
  }
}
