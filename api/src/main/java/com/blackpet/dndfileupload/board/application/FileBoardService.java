package com.blackpet.dndfileupload.board.application;

import com.blackpet.dndfileupload.board.domain.FileBoard;
import com.blackpet.dndfileupload.board.infrastructure.FileBoardRepository;
import com.blackpet.dndfileupload.board.infrastructure.dto.FileBoardResponse;
import com.blackpet.dndfileupload.board.infrastructure.dto.CreateFileBoardRequest;
import com.blackpet.dndfileupload.file.domain.AttachFile;
import com.blackpet.dndfileupload.file.infrastructure.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileBoardService {
  private final FileBoardRepository fileBoardRepository;
  private final FileRepository fileRepository;

  @Transactional
  public UUID create(CreateFileBoardRequest request) {
    // board 먼저 저장하자!
    FileBoard board = new FileBoard(request.getTitle());

    // 첨부파일들을 가져오자!
    List<AttachFile> files = fileRepository.findAllByIdIn(request.getFileIds());

    files.forEach(board::addAttachment);
    fileBoardRepository.save(board);

    return board.getId();
  }

  public List<FileBoardResponse> getList() {
    List<FileBoard> list = fileBoardRepository.findAll();

    return list.stream().map(attachBoard -> {
      FileBoardResponse boardResponse = new FileBoardResponse();
      BeanUtils.copyProperties(attachBoard, boardResponse);
      // exclude non attached file
      boardResponse.setAttachments(attachBoard.getFileBoardAttachmentResponseList());
      return boardResponse;
    }).collect(Collectors.toList());
  }
}
