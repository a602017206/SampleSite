package org.sample.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"${envConfigPath}"})
@Data
public class NewSiteProperties {

    @Value("${createJarTime:}")
    private String createJarTime;

    @Value("${git.commit.time:}")
    private String gitCommitTime;

    /**
     * 公众号认证密钥
     */
    private String accessToken;

    /**
     * 公众号appId
     */
    @Value("${weChat.appId:}")
    private String appId;

    /**
     * 公众号appSecret
     */
    @Value("${weChat.appSecret:}")
    private String appSecret;

    /**
     * 公众号接口认证token
     */
    @Value("${weChat.Token:nnnl}")
    private String weChatToken;


}
