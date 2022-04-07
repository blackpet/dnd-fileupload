package com.blackpet.dndfileupload.file.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FileResponse {
  private UUID id;
  private String originFilename;
}
