package org.sample.web;

import lombok.extern.log4j.Log4j2;
import org.sample.config.SampleSiteProperties;
import org.sample.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 基本测试代码
 */
@RestController
@RequestMapping("/test")
@Log4j2
public class HelloController {

    @Resource
    private UserService userService;

    @Resource
    private SampleSiteProperties properties;

    @RequestMapping("/hello")
    public String hello() {
        return "hello word";
    }


    @RequestMapping(value = "/getName", method = RequestMethod.GET)
    public String getName() {

        try {
            userService.getBaseMapper().selectById(1);
        } catch (Exception e) {
            log.error("失败：", e);
        }

        return "12";
    }

    @RequestMapping(value = "/getProperties", method = RequestMethod.GET)
    public String getProperties(){
        return properties.toString();
    }
}
