package com.blackpet.dndfileupload.board.application;

import com.blackpet.dndfileupload.board.domain.AttachBoard;
import com.blackpet.dndfileupload.board.infrastructure.AttachBoardRepository;
import com.blackpet.dndfileupload.board.infrastructure.dto.CreateAttachBoardRequest;
import com.blackpet.dndfileupload.file.domain.AttachedFile;
import com.blackpet.dndfileupload.file.infrastructure.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachBoardService {
  private final AttachBoardRepository attachBoardRepository;
  private final FileRepository fileRepository;

  @Transactional
  public UUID create(CreateAttachBoardRequest request) {
    // board 먼저 저장하자!
    AttachBoard board = new AttachBoard(request.getTitle());

    // 첨부파일들을 가져오자!
    List<AttachedFile> files = fileRepository.findAllByIdIn(request.getFileIds());

    files.forEach(board::addAttachment);
    attachBoardRepository.save(board);

    return board.getId();
  }
}
