package xyz.proteanbear.capricorn.infrastructure.disruptor;

/**
 * 测试事件
 */
public class ReceiveTestEvent {
    private String content;

    public ReceiveTestEvent() {
    }

    public ReceiveTestEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}