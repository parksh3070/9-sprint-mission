package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.config.DiscodeitRepositoryProperties;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;

@Configuration
@EnableConfigurationProperties(DiscodeitRepositoryProperties.class)
public class AppConfig {

    // =======================
    // UserRepository
    // =======================
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserRepository userRepositoryJcf() {
        return new JCFUserRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public UserRepository userRepositoryFile(DiscodeitRepositoryProperties props) {
        return new FileUserRepository(props.getFileDirectory());
    }

    // =======================
    // ChannelRepository
    // =======================
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public ChannelRepository channelRepositoryJcf() {
        return new JCFChannelRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public ChannelRepository channelRepositoryFile(DiscodeitRepositoryProperties props) {
        return new FileChannelRepository(props.getFileDirectory());
    }

    // =======================
    // MessageRepository
    // =======================
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public MessageRepository messageRepositoryJcf() {
        return new JCFMessageRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public MessageRepository messageRepositoryFile(DiscodeitRepositoryProperties props) {
        return new FileMessageRepository(props.getFileDirectory());
    }

    // =======================
    // ReadStatusRepository
    // =======================
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public ReadStatusRepository readStatusRepositoryJcf() {
        return new JCFReadStatusRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public ReadStatusRepository readStatusRepositoryFile(DiscodeitRepositoryProperties props) {
        return new FileReadStatusRepository(props.getFileDirectory());
    }

    // =======================
    // UserStatusRepository
    // =======================
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserStatusRepository userStatusRepositoryJcf() {
        return new JCFUserStatusRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public UserStatusRepository userStatusRepositoryFile(DiscodeitRepositoryProperties props) {
        return new FileUserStatusRepository(props.getFileDirectory());
    }

    // =======================
    // BinaryContentRepository
    // =======================
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public BinaryContentRepository binaryContentRepositoryJcf() {
        return new JCFBinaryContentRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public BinaryContentRepository binaryContentRepositoryFile(DiscodeitRepositoryProperties props) {
        return new FileBinaryContentRepository(props.getFileDirectory());
    }
    @Bean
    public UserService userService(UserRepository userRepository,
                                   UserStatusRepository userStatusRepository,
                                   BinaryContentRepository binaryContentRepository) {
        return new BasicUserService(userRepository, userStatusRepository, binaryContentRepository);
    }

    @Bean
    public AuthService authService(UserRepository userRepository) {
        return new BasicAuthService(userRepository);
    }

    @Bean
    public ChannelService channelService(ChannelRepository channelRepository,
                                         UserRepository userRepository,
                                         ReadStatusRepository readStatusRepository,
                                         MessageRepository messageRepository) {
        return new BasicChannelService(channelRepository, userRepository, readStatusRepository, messageRepository);
    }

    @Bean
    public MessageService messageService(MessageRepository messageRepository,
                                         ChannelRepository channelRepository,
                                         UserRepository userRepository,
                                         BinaryContentRepository binaryContentRepository) {
        return new BasicMessageService(messageRepository, channelRepository, userRepository, binaryContentRepository);
    }

    @Bean
    public ReadStatusService readStatusService(ReadStatusRepository readStatusRepository,
                                               UserRepository userRepository,
                                               ChannelRepository channelRepository) {
        return new BasicReadStatusService(readStatusRepository, userRepository, channelRepository);
    }




    @Bean
    public UserStatusService userStatusService(UserStatusRepository userStatusRepository,
                                               UserRepository userRepository) {
        return new BasicUserStatusService(userStatusRepository, userRepository);
    }

    @Bean
    public BinaryContentService binaryContentService(BinaryContentRepository binaryContentRepository) {
        return new BasicBinaryContentService(binaryContentRepository);
    }
}