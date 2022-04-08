package com.blackpet.dndfileupload.board.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CreateFileBoardRequest {
  private String title;
  private List<UUID> fileIds;
}
