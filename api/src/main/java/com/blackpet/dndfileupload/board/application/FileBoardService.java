package com.blackpet.dndfileupload.board.application;

import com.blackpet.dndfileupload.board.domain.FileBoard;
import com.blackpet.dndfileupload.board.domain.FileBoardAttachment;
import com.blackpet.dndfileupload.board.infrastructure.FileBoardAttachmentRepository;
import com.blackpet.dndfileupload.board.infrastructure.FileBoardRepository;
import com.blackpet.dndfileupload.board.infrastructure.dto.FileBoardResponse;
import com.blackpet.dndfileupload.board.infrastructure.dto.CreateFileBoardRequest;
import com.blackpet.dndfileupload.board.infrastructure.dto.ModifyFileBoardAttachmentRequest;
import com.blackpet.dndfileupload.board.infrastructure.dto.ModifyFileBoardRequest;
import com.blackpet.dndfileupload.file.domain.AttachFile;
import com.blackpet.dndfileupload.file.infrastructure.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileBoardService {
  private final FileBoardRepository fileBoardRepository;
  private final FileBoardAttachmentRepository fileBoardAttachmentRepository;
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

  @Transactional
  public boolean modify(ModifyFileBoardRequest request) {
    FileBoard board = fileBoardRepository.findById(request.getId()).orElseThrow(RuntimeException::new);

    // update board
    BeanUtils.copyProperties(request, board, "attachments");

    // (기존파일) 파일삭제
    List<UUID> fileIds = deleteFileMarkedAsDeleted(request);

    // (기존파일) attachment 삭제
    List<FileBoardAttachment> tobeDeletedAttachments = fileBoardAttachmentRepository.findAllByFileIdIn(fileIds);
    board.getAttachments().removeAll(tobeDeletedAttachments);

    // 신규 첨부파일 추가하자!
    addNewAttachments(request, board);

    return true;
  }

  /**
   * modify > attachment 싹 갈아치우자!
   * @param request
   * @param board
   */
  private void addNewAttachments(ModifyFileBoardRequest request, FileBoard board) {
    // 다시 싹 잡아넣자! (원래 있던거 + 새로 첨부한거) (삭제한거 빼고)
    List<UUID> fileIds = request.getAttachments().stream()
            .filter(ModifyFileBoardAttachmentRequest::isTemped)
            .map(ModifyFileBoardAttachmentRequest::getFileId).collect(Collectors.toList());

    // 첨부파일들을 가져오자!
    List<AttachFile> files = fileRepository.findAllByIdIn(fileIds);

    files.forEach(board::addAttachment);
  }

  /**
   * modify > (기존파일) 첨부삭제
   * @param request
   * @return
   */
  private List<UUID> deleteFileMarkedAsDeleted(ModifyFileBoardRequest request) {
    List<UUID> fileIds = request.getAttachments().stream()
            .filter(ModifyFileBoardAttachmentRequest::isTobeDeleted)
            .map(ModifyFileBoardAttachmentRequest::getFileId)
            .collect(Collectors.toList());

    // delete files
    fileRepository.findAllByIdIn(fileIds).forEach(f -> {
      try {
        Files.delete(f.getFilePath());
      } catch (IOException e) {
        log.error("{} File does not exists for deleted", f.getName());
      }
    });

    return fileIds;
  }

  public List<FileBoardResponse> getList() {
    List<FileBoard> list = fileBoardRepository.findAll();

    return list.stream().map(board -> {
      FileBoardResponse boardResponse = new FileBoardResponse();
      BeanUtils.copyProperties(board, boardResponse);
      // exclude non attached file
      boardResponse.setAttachments(board.getFileBoardAttachmentResponseList());
      return boardResponse;
    }).collect(Collectors.toList());
  }

  public FileBoardResponse getBoard(UUID id) {
    FileBoard board = fileBoardRepository.findById(id).orElseThrow(RuntimeException::new);

    FileBoardResponse boardResponse = new FileBoardResponse();
    BeanUtils.copyProperties(board, boardResponse);
    // exclude non attached file
    boardResponse.setAttachments(board.getFileBoardAttachmentResponseList());

    return boardResponse;
  }
}
