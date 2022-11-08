package xyz.proteanbear.capricorn.infrastructure.disruptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

/**
 * 单元测试：时间总线测试
 */
public class DisruptorTemplateTest {
    /**
     * 话题接收测试
     */
    @Test
    public void testPublish() throws JsonProcessingException, InterruptedException {
        String topic="TEST_TOPIC_EVENT";
        DisruptorTemplate template=new DisruptorTemplate();
        template.register(topic,new TestTopicEventHandler()).start();
        template.publish("TEST_TOPIC_EVENT",new PublishTestEvent("DisruptorTemplateTest"));
        Thread.sleep(5000);
    }
}