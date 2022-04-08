package com.blackpet.dndfileupload.board.infrastructure;

import com.blackpet.dndfileupload.board.domain.FileBoardAttachment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileBoardAttachmentRepository extends CrudRepository<FileBoardAttachment, UUID> {
}
