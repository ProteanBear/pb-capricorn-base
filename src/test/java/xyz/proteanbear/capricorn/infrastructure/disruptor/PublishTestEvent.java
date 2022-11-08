package xyz.proteanbear.capricorn.infrastructure.disruptor;

/**
 * 测试事件
 */
public class PublishTestEvent {
    private String content;

    public PublishTestEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}