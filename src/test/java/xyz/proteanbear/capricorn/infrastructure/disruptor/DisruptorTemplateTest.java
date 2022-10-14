package xyz.proteanbear.capricorn.infrastructure.disruptor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 单元测试：时间总线测试
 */

public class DisruptorTemplateTest {
    /**
     * 话题接收测试
     */
    @Test
    public void testPublish(){
        String topic="TEST_TOPIC_EVENT";
        DisruptorTemplate template=new DisruptorTemplate();
        template.register(topic,new TestTopicEventHandler()).start();
        template.publish("TEST_TOPIC_EVENT","DisruptorTemplateTest");
    }
}