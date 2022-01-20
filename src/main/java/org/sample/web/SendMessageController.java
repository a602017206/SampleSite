package org.sample.web;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author
 * @desc 队列消息发送消息测试
 */
@RestController
@RequestMapping(value = "/messageQueue")
public class SendMessageController {

    /**
     * kafka topic name
     */
    private final static String TOPIC_NAME = "quickstart-events";

    /**
     * 使用RabbitTemplate,这提供了接收/发送等等方法
     */
    @Resource
    private RabbitTemplate rabbitTemplate;


    /**
     * 使用KafkaTemplate,这提供了接收/发送等等方法
     */
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/sendRabbitMqMessage")
    public String sendRabbitMqMessage(@RequestParam(value = "message", required = false) String message) {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        if (null != message) {
            messageData = message;
        }
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        return "ok";
    }

    @RequestMapping("/sendKafkaMessage")
    public void send(@RequestParam(value = "message", required = false) String message) {

        String messageDate = "hello,kafka";

        if (null != message) {
            messageDate = message;
        }
        String messageId = String.valueOf(UUID.randomUUID());
        //发送功能就一行代码~
        kafkaTemplate.send(TOPIC_NAME,  messageId, messageDate);
    }

}
