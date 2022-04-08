package com.blackpet.dndfileupload.board.infrastructure.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FileBoardResponse {
  private UUID id;
  private String title;
  private List<FileBoardAttachmentResponse> attachments;
}
