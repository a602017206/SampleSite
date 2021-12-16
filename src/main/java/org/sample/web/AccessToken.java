package org.sample.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class AccessToken implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(AccessToken.class);


    @Override
    public void run(String... args) throws Exception {

        for (;;) {
            try {
                String accessTokenDO = "";
                if (accessTokenDO != null) {
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
