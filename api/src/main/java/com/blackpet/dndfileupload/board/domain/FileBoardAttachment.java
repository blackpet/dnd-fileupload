package com.blackpet.dndfileupload.board.domain;

import com.blackpet.dndfileupload.common.entity.BaseAuditingTimeEntity;
import com.blackpet.dndfileupload.file.domain.AttachFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class FileBoardAttachment extends BaseAuditingTimeEntity<FileBoardAttachment> {
  @Id
  @GeneratedValue(generator = "uuid")
  private UUID id;

  @Setter
  @ManyToOne
  @JoinColumn(name = "file_board_id")
  private FileBoard fileBoard;

  @OneToOne
  @JoinColumn(name = "file_id")
  private AttachFile file;

  public FileBoardAttachment(FileBoard fileBoard, AttachFile file) {
    this.fileBoard = fileBoard;
    this.file = file;
  }

  public static FileBoardAttachment create(FileBoard board, AttachFile file) {
    return new FileBoardAttachment(board, file);
  }
}
