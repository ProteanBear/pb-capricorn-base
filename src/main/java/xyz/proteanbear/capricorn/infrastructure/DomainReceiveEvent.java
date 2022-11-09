package xyz.proteanbear.capricorn.infrastructure;

/**
 * <p>基础设施：通用领域事件基类（接收）</p>
 *
 * @author 马强
 */
public class DomainReceiveEvent<T> {
    /**
     * 事件唯一标识
     */
    private String id;

    /**
     * 事件来源
     */
    private String source;

    /**
     * 事件类型标识
     */
    private String topic;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 传递数据
     */
    private T data;

    public DomainReceiveEvent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
