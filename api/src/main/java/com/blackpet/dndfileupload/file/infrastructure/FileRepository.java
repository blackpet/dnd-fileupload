package com.blackpet.dndfileupload.file.infrastructure;


import com.blackpet.dndfileupload.file.domain.AttachFile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends CrudRepository<AttachFile, UUID> {

  List<AttachFile> findAllByIdIn(List<UUID> ids);
}
