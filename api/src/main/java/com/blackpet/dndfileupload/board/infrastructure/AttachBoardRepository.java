package com.blackpet.dndfileupload.board.infrastructure;

import com.blackpet.dndfileupload.board.domain.AttachBoard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttachBoardRepository extends CrudRepository<AttachBoard, UUID> {
}
