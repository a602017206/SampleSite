package org.sample.web;

import org.sample.config.SampleSiteProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @date 2021年12月23日
 * @Description 重复获取有时效的token，微信公众号
 */
@SpringBootApplication
public class AccessToken implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(AccessToken.class);


    @Resource
    private SampleSiteProperties sampleSiteProperties = new SampleSiteProperties();

    @Override
    public void run(String... args) throws Exception {

        for (;;) {
            try {
                String accessTokenDO = "";
                if (accessTokenDO != null) {
                    sampleSiteProperties.setProp(accessTokenDO);
                    logger.info("accessToken : " + accessTokenDO);
                    TimeUnit.SECONDS.sleep(7000);
                } else {
                    TimeUnit.SECONDS.sleep(3000);
                }
            } catch (Exception e) {
                logger.error("create accessToken error", e);
                TimeUnit.SECONDS.sleep(10);
            }

        }
    }
}
