package xyz.proteanbear.capricorn.infrastructure.disruptor;

/**
 * 测试话题消费者
 */
@DisruptorTemplate.DataHandlerTopic("TEST_TOPIC_EVENT")
public class TestTopicEventHandler implements DisruptorTemplate.DataHandler {
    @Override
    public void onReceive(DisruptorTemplate.TopicEvent event) {
        ReceiveTestEvent data;
        try {
            data = event.getData(ReceiveTestEvent.class)
                    .orElseThrow(Exception::new);
            System.out.println("话题「TEST_TOPIC_EVENT」消费内容：" + data.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
