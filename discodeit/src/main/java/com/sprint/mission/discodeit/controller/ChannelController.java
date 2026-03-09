package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    // 1) 공개 채널 생성
    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public Channel createPublic(@RequestBody ChannelCreatePublicRequest request) {
        return channelService.createPublic(request);
    }

    // 2) 비공개 채널 생성
    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public Channel createPrivate(@RequestBody ChannelCreatePrivateRequest request) {
        return channelService.createPrivate(request);
    }

    // 3) 공개 채널 정보 수정
    @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
    public Channel update(@PathVariable UUID channelId,
                          @RequestBody ChannelUpdateRequest request) {

        ChannelUpdateRequest newRequest = new ChannelUpdateRequest(
                channelId,
                request.getNewName(),
                request.getNewDescription()
        );

        return channelService.update(newRequest);
    }

    // 4) 채널 삭제
    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
    }

    // 5) 특정 사용자가 볼 수 있는 모든 채널 목록 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<Channel> findAllByUserId(@RequestParam UUID userId) {
        return channelService.findAllByUserId(userId);
    }
}