package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaApplication {

    static UserResponse setupUser(UserService userService) {
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

    static void messageCreateTest(MessageService messageService, Channel channel, UserResponse author) {
        Message message = messageService.create(new MessageCreateRequest(
                "안녕하세요.",
                channel.getId(),
                author.id(),
                null
        ));
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        UserResponse user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        messageCreateTest(messageService, channel, user);
    }
}