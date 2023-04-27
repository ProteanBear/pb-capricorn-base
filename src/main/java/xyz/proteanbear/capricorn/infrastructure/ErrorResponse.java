package xyz.proteanbear.capricorn.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * <p>基础设施：错误时返回信息格式定义</p>
 *
 * @author 马强
 */
public class ErrorResponse {
    /**
     * 创建错误返回内容对象
     *
     * @param request   请求
     * @param exception 异常
     * @return 返回对象
     */
    public static ErrorResponse of(HttpServletRequest request, HttpStatus httpStatus, Exception exception) {
        ErrorResponse response=new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(httpStatus.value());
        response.setError(httpStatus.getReasonPhrase());
        response.setMessage(exception.getMessage());
        response.setPath(request.getRequestURI());
        return response;
    }

    /**
     * 创建错误返回内容对象
     *
     * @param request   请求
     * @param exception 异常
     * @return 返回对象
     */
    public static ErrorResponse of(HttpServletRequest request, HttpStatus httpStatus, Exception exception,String message) {
        ErrorResponse response=of(request, httpStatus, exception);
        response.setMessage(message);
        return response;
    }

    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
