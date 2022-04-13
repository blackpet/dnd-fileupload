package com.blackpet.dndfileupload.board.infrastructure;

import com.blackpet.dndfileupload.board.domain.FileBoardAttachment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileBoardAttachmentRepository extends CrudRepository<FileBoardAttachment, UUID> {
  void deleteByFileIdIn(List<UUID> fileIds);
  List<FileBoardAttachment> findAllByFileIdIn(List<UUID> fileIds);
}
