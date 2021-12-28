package org.sample.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 全局基础配置
 */
@Configuration
@Data
public class SampleSiteProperties {

    /**
     * jar包生成时间（可用于校验是否是新包）
     */
    @Value("${createJarTime:}")
    private String createJarTime;

    /**
     * 当前打包commit时间（可用于校验包）
     */
    @Value("${git.commit.time:}")
    private String gitCommitTime;

    @Value("${test.prop:huhu}")
    private String prop;

}
