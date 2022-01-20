package org.sample.consumer;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author
 * @desc kafka消费类
 */
//@Component
@Log4j2
public class KafkaConsumer {

    /**
     * kafka的监听器，topic为"quickstart-events"，消费者组为"quickstart-events"
     */
    @KafkaListener(topics = "quickstart-events", groupId = "quickstart-events")
    public void listenGroup(ConsumerRecord<String, String> record) {
//        String value = record.get("payload");
//        log.info("kafka消息内容: {}", value);
        log.info("kafka消息体: {}", record);
        //手动提交offset
//        ack.acknowledge();
    }

    /*//kafka的监听器，topic为"zhTest"，消费者组为"zhTestGroup"
    @KafkaListener(topics = "zhTest", groupId = "zhTestGroup")
    public void listenZhugeGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println(value);
        System.out.println(record);
        //手动提交offset
        ack.acknowledge();
    }*/

}
