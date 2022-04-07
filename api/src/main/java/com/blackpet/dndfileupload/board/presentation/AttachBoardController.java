package com.blackpet.dndfileupload.board.presentation;

import com.blackpet.dndfileupload.board.application.AttachBoardService;
import com.blackpet.dndfileupload.board.infrastructure.dto.CreateAttachBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AttachBoardController {

  private final AttachBoardService attachBoardService;

  @PostMapping("/attachBoards")
  public ResponseEntity<UUID> create(@RequestBody CreateAttachBoardRequest request) {
    UUID newId = attachBoardService.create(request);

    return ResponseEntity.ok(newId);
  }
}
