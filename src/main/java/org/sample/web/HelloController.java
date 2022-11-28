package org.sample.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.sample.config.SampleSiteProperties;
import org.sample.domain.User;
import org.sample.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


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
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("key", "hello word");
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }


    @RequestMapping(value = "/getName", method = RequestMethod.GET)
    public String getName() {

        try {
            User user = userService.getBaseMapper().selectById(1);
            if (null != user) {
                return user.getName();
            }
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
