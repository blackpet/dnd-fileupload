package com.blackpet.dndfileupload.board.presentation;

import com.blackpet.dndfileupload.board.application.FileBoardService;
import com.blackpet.dndfileupload.board.infrastructure.dto.CreateFileBoardRequest;
import com.blackpet.dndfileupload.board.infrastructure.dto.FileBoardResponse;
import com.blackpet.dndfileupload.board.infrastructure.dto.ModifyFileBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fileBoards")
public class FileBoardController {

  private final FileBoardService fileBoardService;

  @PostMapping
  public ResponseEntity<UUID> create(@RequestBody CreateFileBoardRequest request) {
    UUID newId = fileBoardService.create(request);

    return ResponseEntity.ok(newId);
  }

  @PutMapping
  public ResponseEntity<Boolean> modify(@RequestBody ModifyFileBoardRequest request) {
    boolean res = fileBoardService.modify(request);

    return ResponseEntity.ok(res);
  }

  @GetMapping
  public ResponseEntity<List<FileBoardResponse>> getList() {
    List<FileBoardResponse> list = fileBoardService.getList();

    return ResponseEntity.ok(list);
  }

  @GetMapping("/{id}")
  public ResponseEntity<FileBoardResponse> getBoard(@PathVariable UUID id) {
    FileBoardResponse board = fileBoardService.getBoard(id);

    return ResponseEntity.ok(board);
  }
}
