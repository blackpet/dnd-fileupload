package com.blackpet.dndfileupload.file.domain;

import com.blackpet.dndfileupload.common.entity.BaseAuditingTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class AttachFile extends BaseAuditingTimeEntity<AttachFile> {
  @Id
  private UUID id;

  private String name;
  private String contentType;
  private long size;
  private String path;
  private boolean attached;

  public AttachFile(String name, String contentType, long size, Path uploadDirectory) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.contentType = contentType;
    this.size = size;
    this.setPath(uploadDirectory);
  }

  public void setPath(Path uploadDirectory) {
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    this.path = uploadDirectory
            .resolve(today)
            .resolve(this.getId().toString())
            .toString();
  }

  public Path getFilePath() {
    return Paths.get(this.path);
  }

  public void attach() {
    this.attached = true;
  }

  public void verifyDownload() {
    if (!this.attached) {
      throw new RuntimeException("첨부되지 않은 파일입니다.");
    }
  }
}
