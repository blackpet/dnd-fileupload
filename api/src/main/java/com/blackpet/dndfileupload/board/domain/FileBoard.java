package com.blackpet.dndfileupload.board.domain;

import com.blackpet.dndfileupload.board.infrastructure.dto.FileBoardAttachmentResponse;
import com.blackpet.dndfileupload.common.entity.BaseAuditingTimeEntity;
import com.blackpet.dndfileupload.file.domain.AttachFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
public class FileBoard extends BaseAuditingTimeEntity<FileBoard> {
  @Id
  @GeneratedValue(generator = "uuid")
  private UUID id;
  private String title;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "fileBoard", cascade = CascadeType.ALL)
  private List<FileBoardAttachment> attachments = new ArrayList<>();

  public FileBoard(String title) {
    this.title = title;
  }

  public void addAttachment(AttachFile f) {
    // 첨부완료!!
    f.attach();

    FileBoardAttachment attachment = FileBoardAttachment.create(this, f);
    this.attachments.add(attachment);
  }

  /**
   * 유효한(첨부완료된) 파일만 포함한 Attachment 목록
   * @return
   */
  public List<FileBoardAttachmentResponse> getFileBoardAttachmentResponseList() {
    return attachments.stream()
            .filter(att -> att.getFile().isAttached()) // attached == true 만 뽑자!
            .map(att -> {
              FileBoardAttachmentResponse res = new FileBoardAttachmentResponse();
              BeanUtils.copyProperties(att.getFile(), res);
              res.setAttachmentId(att.getId());
              res.setFileId(att.getFile().getId());

              return res;
            })
            .collect(Collectors.toList());
  }
}
