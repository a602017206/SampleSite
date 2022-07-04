package org.sample.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.http.entity.ContentType;
import org.sample.config.SampleSiteProperties;
import org.sample.domain.User;
import org.sample.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    @GetMapping("/createFile")
    public void createFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String a = "qweqweqew";
        try {
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=html.txt");
            // 响应类型,编码
            httpServletResponse.setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
            outputStream.write(a.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/cqHttp")
    public void cqHttp(@RequestBody Map<String, Object> map){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info(objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        String a = "\\d{4}[-]\\d{2}[-]\\d{2}T";

        Map<String, String> map = new HashMap<>();
        map.put("x", a);

        System.out.println(map.get("x"));

        try {
            String s = objectMapper.writeValueAsString(map);

            System.out.println(s);

            Map map1 = objectMapper.readValue(s, Map.class);

            System.out.println(map1);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

}
