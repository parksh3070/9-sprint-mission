package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    // 1️⃣ 특정 채널의 메시지 수신 정보 생성
    @RequestMapping(method = RequestMethod.POST)
    public ReadStatus create(@RequestBody ReadStatusCreateRequest request) {
        return readStatusService.create(request);
    }

    // 2️⃣ 특정 채널의 메시지 수신 정보 수정
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ReadStatus update(@PathVariable UUID id,
                             @RequestBody ReadStatusUpdateRequest request) {

        ReadStatusUpdateRequest newRequest = new ReadStatusUpdateRequest(
                id
        );

        return readStatusService.update(newRequest);
    }

    // 3️⃣ 특정 사용자의 메시지 수신 정보 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<ReadStatus> findAllByUserId(@RequestParam UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }
}