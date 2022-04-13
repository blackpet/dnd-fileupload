package com.blackpet.dndfileupload.board.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ModifyFileBoardAttachmentRequest {
  private UUID fileId;
  private boolean tobeDeleted; // 삭제 대상 파일
  private boolean temped; // 신규 첨부 파일
}
