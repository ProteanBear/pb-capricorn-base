package xyz.proteanbear.capricorn.infrastructure.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import xyz.proteanbear.capricorn.infrastructure.util.SpringContextUtil;

import javax.annotation.PostConstruct;
import java.lang.annotation.*;
import java.util.*;

/**
 * 高性能内存消息队列，用于实现单应用的消息总线。
 *
 * @author 马强
 */
@Component
@Scope("singleton")
@Lazy
public class DisruptorTemplate {
    /**
     * 默认缓存区大小
     */
    private static final int defaultBufferSize = 512;

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DisruptorTemplate.class);

    /**
     * 消息事件
     */
    public static class TopicEvent {
        /**
         * 设置消息事件的话题标识
         */
        private String topic;

        /**
         * 传递消息事件的数据
         */
        private Object data;

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public Object getData() {
            return data;
        }

        @SuppressWarnings("unchecked")
        public <T> T getData(Class<T> clazz) {
            return this.getClass().isAssignableFrom(clazz) ? ((T) this) : null;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    /**
     * 消息事件生产工厂
     */
    public static class TopicEventFactory implements EventFactory<TopicEvent> {
        @Override
        public TopicEvent newInstance() {
            return new TopicEvent();
        }
    }

    /**
     * 事件消费者实现数据处理接口
     */
    public interface DataHandler {
        /**
         * 接收数据
         */
        <T> void onReceive(T data);
    }

    /**
     * 时间消费者话题标识注解
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface DataHandlerTopic {
        String value();
    }

    /**
     * 当前的消息队列
     */
    private Disruptor<TopicEvent> disruptor;

    /**
     * 获取配置项中的缓存区大小
     */
    @Value("${capricorn.disruptor.buffer-size}")
    private Integer bufferSize;

    /**
     * 存储当前应用中的消费者与话题的对应关系
     */
    private final Map<String, List<DataHandler>> topicHandlerMap = new HashMap<>();

    /**
     * 发布者事件转换器
     */
    private EventTranslatorTwoArg<TopicEvent, String, Object> translator;

    /**
     * 创建新的消息总线处理
     * 检查项目中的全部消费者并注册
     */
    @PostConstruct
    public void start() {
        logger.info("Initialize the message queue in the DisruptorTemplate.");

        //载入并登记消费者
        int registerNumber = this.loadProcessors();
        logger.info("Successfully initialized the corresponding topics of the component" +
                ", " + topicHandlerMap.keySet().size() + " topics and " + registerNumber + " consumers.");

        //创建事件分发者
        logger.info("Start creating message queue.");
        int bufferSize = Optional.ofNullable(this.bufferSize).orElse(defaultBufferSize);
        disruptor = new Disruptor<>(new TopicEventFactory(), bufferSize,
                new CustomizableThreadFactory("disruptor-executor-"),
                ProducerType.SINGLE, new YieldingWaitStrategy()
        );
        //设置消费者
        //依据话题调用对应的订阅者进行处理
        disruptor.handleEventsWith((topicEvent, sequence, endOfBatch) -> {
            if (!topicHandlerMap.containsKey(topicEvent.getTopic())) return;
            topicHandlerMap.getOrDefault(topicEvent.getTopic(), List.of())
                    .forEach(handler -> handler.onReceive(topicEvent.getData()));
        });
        //开启
        logger.info("Disruptor Message Queue is starting.");
        disruptor.start();
        logger.info("Disruptor Message Queue is started.");
        //设置转换器
        translator = (topicEvent, sequence, topic, data) -> {
            topicEvent.setTopic(topic);
            topicEvent.setData(data);
        };
    }

    /**
     * 事件发布
     */
    public <T> void publish(String topic, T data) {
        if (this.disruptor == null) return;
        RingBuffer<TopicEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(translator, topic, data);
    }

    /**
     * 手动登记消费者
     */
    public DisruptorTemplate register(String topic, DataHandler dataHandler) {
        List<DataHandler> handlers = topicHandlerMap.getOrDefault(topic, new ArrayList<>());
        handlers.add(dataHandler);
        topicHandlerMap.put(topic, handlers);
        return this;
    }

    /**
     * 通过Spring上下文环境载入当前项目中全部注解的消费者，并记录下来
     *
     * @return 返回此次登记的消费者数量
     */
    private int loadProcessors() {
        if (SpringContextUtil.getApplicationContext() == null) return 0;
        //从Spring上下文中获取使用消费者注解的组件
        Map<String, ?> handlerObjectMap = SpringContextUtil.getBeansWithAnnotation(DataHandlerTopic.class);
        for (Map.Entry<String, ?> stringEntry : handlerObjectMap.entrySet()) {
            DataHandlerTopic handlerTopic = SpringContextUtil.getApplicationContext()
                    .findAnnotationOnBean(stringEntry.getKey(), DataHandlerTopic.class);
            if (handlerTopic == null) continue;
            Object handlerObject = stringEntry.getValue();
            //获取指定的频道对应的组件列表，并增加当前的消费者对应
            if (handlerObject instanceof DataHandler) {
                List<DataHandler> handlers = topicHandlerMap.getOrDefault(handlerTopic.value(), new ArrayList<>());
                handlers.add((DataHandler) handlerObject);
                topicHandlerMap.put(handlerTopic.value(), handlers);
            }
        }
        return handlerObjectMap.values().size();
    }
}