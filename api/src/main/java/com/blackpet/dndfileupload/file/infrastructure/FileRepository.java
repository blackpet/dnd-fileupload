package com.blackpet.dndfileupload.file.infrastructure;


import com.blackpet.dndfileupload.file.domain.AttachedFile;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FileRepository extends CrudRepository<AttachedFile, UUID> {
}
