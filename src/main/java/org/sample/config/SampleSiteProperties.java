package org.sample.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class SampleSiteProperties {

    @Value("${createJarTime:}")
    private String createJarTime;

    @Value("${git.commit.time:}")
    private String gitCommitTime;

    @Value("${test.prop:huhu}")
    private String prop;

}
