package xyz.proteanbear.capricorn.infrastructure.disruptor;

/**
 * 测试话题消费者
 */
@DisruptorTemplate.DataHandlerTopic("TEST_TOPIC_EVENT")
public class TestTopicEventHandler implements DisruptorTemplate.DataHandler {
    @Override
    public <T> void onReceive(T data) {
        System.out.println("话题「TEST_TOPIC_EVENT」消费内容："+data);
    }
}
