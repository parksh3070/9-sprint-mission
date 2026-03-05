package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.MessageUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // 1) 메시지 전송
    @RequestMapping(method = RequestMethod.POST)
    public Message create(@RequestBody MessageCreateRequest request) {
        return messageService.create(request);
    }

    // 2) 메시지 수정
    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public Message update(@PathVariable UUID messageId,
                          @RequestBody MessageUpdateRequest request) {

        MessageUpdateRequest newRequest = new MessageUpdateRequest(
                messageId,
                request.getNewContent()
        );

        return messageService.update(newRequest);
    }

    // 3) 메시지 삭제
    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
    }

    // 4) 특정 채널의 메시지 목록 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<Message> findAllByChannelId(@RequestParam UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }
}