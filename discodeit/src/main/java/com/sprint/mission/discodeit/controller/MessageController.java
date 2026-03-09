package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // 1) 메시지 전송
    @PostMapping
    public Message create(@RequestBody MessageCreateRequest request) {
        return messageService.create(request);
    }

    // 2) 메시지 수정
    @PutMapping("/{messageId}")
    public Message update(@PathVariable UUID messageId,
                          @RequestBody MessageUpdateRequest request) {

        MessageUpdateRequest newRequest = new MessageUpdateRequest(
                messageId,
                request.getNewContent()
        );

        return messageService.update(newRequest);
    }

    // 3) 메시지 삭제
    @DeleteMapping("/{messageId}")
    public void delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
    }

    // 4) 특정 채널의 메시지 목록 조회 (50개씩, 최근순)
    @GetMapping
    public PageResponse<MessageResponse> findMessages(
            @RequestParam UUID channelId,
            @RequestParam(defaultValue = "0") int page
    ) {
        return messageService.findMessages(channelId, page);
    }
}