package xyz.proteanbear.capricorn.infrastructure;

/**
 * <p>基础设施：通用领域事件基类</p>
 *
 * @author 马强
 */
public class DomainEvent<T> {
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
    private final T data;

    public DomainEvent(T data) {
        this.data = data;
    }

    public DomainEvent(String source, T data) {
        this.source = source;
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

    public long getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }
}
