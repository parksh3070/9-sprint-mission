package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentService {

    BinaryContentDto create(BinaryContentCreateRequest request);

    BinaryContentDto find(UUID id);

    List<BinaryContentDto> findAllByIdIn(List<UUID> ids);

    ResponseEntity<?> download(UUID id);

    void delete(UUID id);
}