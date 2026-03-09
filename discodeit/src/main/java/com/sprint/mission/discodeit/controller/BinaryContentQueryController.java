package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
public class BinaryContentQueryController {

    private final BinaryContentService binaryContentService;

    public BinaryContentQueryController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentDto> find(@RequestParam UUID binaryContentId) {
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }
}