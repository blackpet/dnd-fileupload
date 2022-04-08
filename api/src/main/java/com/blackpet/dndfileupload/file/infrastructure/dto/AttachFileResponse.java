package com.blackpet.dndfileupload.file.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachFileResponse {
  private UUID id;
  private String originFilename;
  private String contentType;
  private long size;
  private boolean attached;
}
