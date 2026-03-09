package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    @GetMapping("/{id}")
    public BinaryContentDto findOne(@PathVariable UUID id) {
        return binaryContentService.find(id);
    }

    @GetMapping
    public List<BinaryContentDto> findMany(@RequestParam List<UUID> ids) {
        return binaryContentService.findAllByIdIn(ids);
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
        return binaryContentService.download(binaryContentId);
    }
}