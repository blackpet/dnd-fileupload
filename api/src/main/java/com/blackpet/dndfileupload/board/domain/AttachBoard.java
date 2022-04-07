package com.blackpet.dndfileupload.board.domain;

import com.blackpet.dndfileupload.common.entity.BaseAuditingTimeEntity;
import com.blackpet.dndfileupload.file.domain.AttachedFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class AttachBoard extends BaseAuditingTimeEntity<AttachBoard> {
  @Id
  @GeneratedValue(generator = "uuid")
  private UUID id;
  private String title;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "attachBoard", cascade = CascadeType.ALL)
  private List<AttachBoardAttachment> attachments = new ArrayList<>();

  public AttachBoard(String title) {
    this.title = title;
  }

  public void addAttachment(AttachedFile f) {
    // 첨부완료!!
    f.attach();

    AttachBoardAttachment attachment = AttachBoardAttachment.create(this, f);
    this.attachments.add(attachment);
  }
}
