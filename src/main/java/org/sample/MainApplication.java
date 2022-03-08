package org.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


@Configuration
@SpringBootApplication(scanBasePackages = "org.sample")
@MapperScan({"org.sample.mapper"})
//@NacosPropertySource(dataId = "SampleSite", autoRefreshed = true)
public class MainApplication {

	static Logger logger = LoggerFactory.getLogger(MainApplication.class);
	
	public static void main(String[] args) {
	    SpringApplication.run(MainApplication.class, args);
	    logger.info(" ------------ start success -----------");
	}

}
