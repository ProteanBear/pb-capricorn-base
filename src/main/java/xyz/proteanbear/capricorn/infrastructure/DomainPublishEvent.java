package xyz.proteanbear.capricorn.infrastructure;

import java.util.UUID;

/**
 * <p>基础设施：通用领域事件基类（发布）</p>
 *
 * @author 马强
 */
public class DomainPublishEvent<T> {
    /**
     * 事件唯一标识
     */
    private final String id;

    /**
     * 事件来源
     */
    private final String source;

    /**
     * 事件类型标识
     */
    private String topic;

    /**
     * 时间戳
     */
    private final long timestamp;

    /**
     * 传递数据
     */
    private final T data;

    public DomainPublishEvent(String source, T data) {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.source = source;
    }

    public DomainPublishEvent(String id, String source, T data) {
        this.id = id;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.source = source;
    }

    public DomainPublishEvent(String id, String source, String topic, T data) {
        this.id = id;
        this.source = source;
        this.topic = topic;
        this.timestamp = System.currentTimeMillis();
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }
}
