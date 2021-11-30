package org.sample;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@SpringBootApplication(scanBasePackages = "org.sample")
@NacosPropertySource(dataId = "sample-site", autoRefreshed = true)
public class MainApplication {

	@NacosInjected
	private NamingService namingService;

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${server.port}")
	private Integer serverPort;

	static Logger logger = LoggerFactory.getLogger(MainApplication.class);

	//定义静态的ApplicationContext
    public static ConfigurableApplicationContext applicationContext;
	
	public static void main(String[] args) {
	    SpringApplication.run(MainApplication.class, args);
	    logger.info(" ------------ start success -----------");
	}

	@PostConstruct
	public void registerService() throws NacosException {
		namingService.registerInstance(applicationName, "82.157.129.159", serverPort);
	}

}
