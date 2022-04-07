package com.blackpet.dndfileupload.board.domain;

import com.blackpet.dndfileupload.common.entity.BaseAuditingTimeEntity;
import com.blackpet.dndfileupload.file.domain.AttachedFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class AttachBoardAttachment extends BaseAuditingTimeEntity<AttachBoardAttachment> {
  @Id
  @GeneratedValue(generator = "uuid")
  private UUID id;

  @Setter
  @ManyToOne
  @JoinColumn(name = "attach_board_id")
  private AttachBoard attachBoard;

  @OneToOne
  @JoinColumn(name = "file_id")
  private AttachedFile file;

  public AttachBoardAttachment(AttachBoard attachBoard, AttachedFile file) {
    this.attachBoard = attachBoard;
    this.file = file;
  }

  public static AttachBoardAttachment create(AttachBoard board, AttachedFile file) {
    return new AttachBoardAttachment(board, file);
  }
}
