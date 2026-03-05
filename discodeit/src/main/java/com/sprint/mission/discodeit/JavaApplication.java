package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.service.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.UserCreateRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaApplication {

    static User setupUser(UserService userService) {
        return userService.create(new UserCreateRequest(
                "woody",
                "woody@codeit.com",
                "woody1234",
                null
        ));
    }

    static Channel setupChannel(ChannelService channelService) {
        return channelService.createPublic(new ChannelCreatePublicRequest(
                "공지",
                "공지 채널입니다."
        ));
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create(new MessageCreateRequest(
                "안녕하세요.",
                channel.getId(),
                author.getId(),
                null
        ));
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // ✅ Spring 실행 (DiscodeitApplication을 기준으로 컨텍스트 생성)
        ConfigurableApplicationContext context =
                SpringApplication.run(DiscodeitApplication.class, args);

        // ✅ Bean 꺼내기
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // ✅ 셋업 + 테스트
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        messageCreateTest(messageService, channel, user);
    }
}