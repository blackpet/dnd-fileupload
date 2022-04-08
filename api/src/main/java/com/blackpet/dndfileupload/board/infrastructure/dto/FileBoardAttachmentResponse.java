package com.blackpet.dndfileupload.board.infrastructure.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class FileBoardAttachmentResponse {
  private UUID attachmentId;
  private UUID fileId;
  private String originFilename;
  private String contentType;
  private long size;
  private boolean attached;
}
