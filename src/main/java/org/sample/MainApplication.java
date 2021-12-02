package org.sample;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;


@Configuration
@SpringBootApplication(scanBasePackages = "org.sample")
@NacosPropertySource(dataId = "SampleSite", autoRefreshed = true)
public class MainApplication {

	static Logger logger = LoggerFactory.getLogger(MainApplication.class);

	//定义静态的ApplicationContext
    public static ConfigurableApplicationContext applicationContext;
	
	public static void main(String[] args) {
	    SpringApplication.run(MainApplication.class, args);
	    logger.info(" ------------ start success -----------");
	}

}
