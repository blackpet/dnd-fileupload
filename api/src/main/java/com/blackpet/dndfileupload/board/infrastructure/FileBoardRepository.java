package com.blackpet.dndfileupload.board.infrastructure;

import com.blackpet.dndfileupload.board.domain.FileBoard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileBoardRepository extends CrudRepository<FileBoard, UUID> {
  List<FileBoard> findAll();
}
