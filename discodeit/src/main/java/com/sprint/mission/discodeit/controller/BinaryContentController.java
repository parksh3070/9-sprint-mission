package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-contents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    // 1) 바이너리 파일 1개 조회
    // GET /api/binary-contents/{id}
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BinaryContent findOne(@PathVariable UUID id) {
        return binaryContentService.find(id);
    }

    // 2) 바이너리 파일 여러 개 조회
    // GET /api/binary-contents?ids=uuid1,uuid2,...
    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContent> findMany(@RequestParam List<UUID> ids) {
        return binaryContentService.findAllByIdIn(ids);
    }
}