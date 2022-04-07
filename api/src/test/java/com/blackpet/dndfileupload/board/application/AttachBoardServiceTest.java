package com.blackpet.dndfileupload.board.application;

import com.blackpet.dndfileupload.board.domain.AttachBoard;
import com.blackpet.dndfileupload.board.infrastructure.AttachBoardRepository;
import com.blackpet.dndfileupload.board.infrastructure.dto.CreateAttachBoardRequest;
import com.blackpet.dndfileupload.file.domain.AttachedFile;
import com.blackpet.dndfileupload.file.infrastructure.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttachBoardServiceTest {

  private List<AttachedFile> files = new ArrayList<>();

  @Value("${upload.directory}")
  private String uploadDirectory;

  @Autowired
  private FileRepository fileRepository;
  @Autowired
  private AttachBoardRepository attachBoardRepository;
  @Autowired
  private AttachBoardService attachBoardService;

  @BeforeEach
  void setUp() {
    Path targetDirectory = Paths.get(uploadDirectory);
    files.add(new AttachedFile("aaa", "image/jpg", 100, targetDirectory));
    files.add(new AttachedFile("bbb", "application/xlsx", 200, targetDirectory));
    files.add(new AttachedFile("ccc", "application/hwp", 300, targetDirectory));

    fileRepository.saveAll(files);
  }

  @Test
  @DisplayName("Create AttachBoard")
  void create() {
    //given
    String title = "첨부파일들 1";
    List<UUID> fileIds = files.stream().map(AttachedFile::getId).collect(Collectors.toList());
    CreateAttachBoardRequest request = CreateAttachBoardRequest.builder()
            .title(title)
            .fileIds(fileIds)
            .build();

    UUID newId = attachBoardService.create(request);

    //when
    AttachBoard board = attachBoardRepository.findById(newId).orElseThrow();

    //then
    assertAll(
            () -> assertEquals(newId, board.getId()),
            () -> assertEquals(title, board.getTitle()),
            () -> assertEquals(fileIds.size(), board.getAttachments().size()),
            () -> assertTrue(fileIds.contains(board.getAttachments().get(0).getFile().getId())),
            () -> assertTrue(board.getAttachments().get(0).getFile().isAttached())
    );

  }

}
