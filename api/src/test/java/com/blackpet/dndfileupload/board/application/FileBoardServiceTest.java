package com.blackpet.dndfileupload.board.application;

import com.blackpet.dndfileupload.board.domain.FileBoard;
import com.blackpet.dndfileupload.board.domain.FileBoardAttachment;
import com.blackpet.dndfileupload.board.infrastructure.FileBoardRepository;
import com.blackpet.dndfileupload.board.infrastructure.dto.CreateFileBoardRequest;
import com.blackpet.dndfileupload.board.infrastructure.dto.ModifyFileBoardAttachmentRequest;
import com.blackpet.dndfileupload.board.infrastructure.dto.ModifyFileBoardRequest;
import com.blackpet.dndfileupload.file.domain.AttachFile;
import com.blackpet.dndfileupload.file.infrastructure.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileBoardServiceTest {

  private List<AttachFile> files = new ArrayList<>();

  @Value("${upload.directory}")
  private String uploadDirectory;

  @Autowired
  private FileRepository fileRepository;
  @Autowired
  private FileBoardRepository fileBoardRepository;
  @Autowired
  private FileBoardService fileBoardService;

  @BeforeEach
  void setUp() {
    Path targetDirectory = Paths.get(uploadDirectory);
    files.add(new AttachFile("aaa", "image/jpg", 100, targetDirectory));
    files.add(new AttachFile("bbb", "application/xlsx", 200, targetDirectory));
    files.add(new AttachFile("ccc", "application/hwp", 300, targetDirectory));

    fileRepository.saveAll(files);
  }

  @Test
  @DisplayName("Create AttachBoard")
  @Commit
  @Order(1)
  void create() {
    //given
    String title = "첨부파일들 1";
    List<UUID> fileIds = files.stream().map(AttachFile::getId).collect(Collectors.toList());
    CreateFileBoardRequest request = CreateFileBoardRequest.builder()
            .title(title)
            .fileIds(fileIds)
            .build();

    UUID newId = fileBoardService.create(request);

    //when
    FileBoard board = fileBoardRepository.findById(newId).orElseThrow();

    //then
    assertAll(
            () -> assertEquals(newId, board.getId()),
            () -> assertEquals(title, board.getTitle()),
            () -> assertEquals(fileIds.size(), board.getAttachments().size()),
            () -> assertTrue(fileIds.contains(board.getAttachments().get(0).getFile().getId())),
            () -> assertTrue(board.getAttachments().get(0).getFile().isAttached())
    );

  }

  @Test
  @Order(2)
  void modify() {
    //given
    FileBoard testBoard = fileBoardRepository.findAll().stream().findFirst().orElseThrow();
    List<AttachFile> testFiles = testBoard.getAttachments().stream().map(FileBoardAttachment::getFile).collect(Collectors.toList());

    // 첨부파일 중에 한놈을 삭제 대상으로 지정하자!
    List<ModifyFileBoardAttachmentRequest> attachmentRequests = testFiles.stream()
            .map(f -> ModifyFileBoardAttachmentRequest.builder()
                    .fileId(f.getId())
                    .build())
            .collect(Collectors.toList());

    attachmentRequests.get(0).setTobeDeleted(true);

    // 신규 첨부파일 한놈 추가하자!
    AttachFile newFile1 = new AttachFile("fff", "image/jpg", 100, Paths.get(uploadDirectory));
    AttachFile newFile2 = new AttachFile("ggg", "image/jpg", 100, Paths.get(uploadDirectory));
    fileRepository.saveAll(List.of(newFile1, newFile2));

    attachmentRequests.add(ModifyFileBoardAttachmentRequest.builder()
            .fileId(newFile1.getId())
            .temped(true)
            .build());
    attachmentRequests.add(ModifyFileBoardAttachmentRequest.builder()
            .fileId(newFile2.getId())
            .temped(true)
            .build());

    String title = "수정된 첨부파일!";
    ModifyFileBoardRequest request = ModifyFileBoardRequest.builder()
            .id(testBoard.getId())
            .title(title)
            .attachments(attachmentRequests)
            .build();

    //when
    boolean modified = fileBoardService.modify(request);
    FileBoard board = fileBoardRepository.findById(testBoard.getId()).orElseThrow();

    //then
    assertAll(
            () -> assertEquals(title, board.getTitle()),
            () -> assertEquals(testFiles.size() + 1, board.getAttachments().size()) // -1 +2 = +1
    );
  }
}
