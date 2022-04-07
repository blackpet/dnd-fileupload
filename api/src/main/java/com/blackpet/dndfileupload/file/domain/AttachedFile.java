package com.blackpet.dndfileupload.file.domain;

import com.blackpet.dndfileupload.common.entity.BaseAuditingTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class AttachedFile extends BaseAuditingTimeEntity<AttachedFile> {
  public AttachedFile(String originFilename, String contentType, long size, Path uploadDirectory) {
    this.id = UUID.randomUUID();
    this.originFilename = originFilename;
    this.contentType = contentType;
    this.size = size;
    this.setPath(uploadDirectory);
  }

  @Id
  private UUID id;

  private String originFilename;
  private String contentType;
  private long size;
  private String path;
  private boolean attached;

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
    if (this.attached) throw new RuntimeException("이미 첨부된 파일입니다. id=" + this.id);
    this.attached = true;
  }
}
