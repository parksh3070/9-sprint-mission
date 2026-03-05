package com.sprint.mission.discodeit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "discodeit.repository")
public class DiscodeitRepositoryProperties {
    /**
     * jcf | file
     */
    private String type = "jcf";

    /**
     * 파일 저장 디렉토리 (프로젝트 루트 기준 상대경로)
     */
    private String fileDirectory = ".discodeit";
}