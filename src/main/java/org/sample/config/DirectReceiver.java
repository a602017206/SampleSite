package org.sample.config;

import lombok.extern.log4j.Log4j2;

import java.util.Map;

/**
 * @author
 * RabbitMq消息监听
 */
//@Component
//@RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
@Log4j2
public class DirectReceiver {

//    @RabbitHandler
    public void process(Map testMessage) {
        log.info("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }

}
