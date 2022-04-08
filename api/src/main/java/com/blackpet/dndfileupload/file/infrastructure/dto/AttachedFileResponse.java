package com.blackpet.dndfileupload.file.infrastructure.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AttachedFileResponse {
  private UUID id;
  private String originFilename;
  private String contentType;
  private long size;
  private boolean attached;
}
