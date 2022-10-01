package xyz.proteanbear.capricorn.infrastructure.auth;

/**
 * 鉴权失败异常
 *
 * @author 马强
 */
public class AuthorityFailureException extends Exception {
    public AuthorityFailureException() {
        super("用户未登录，或没有此服务接口的使用权限");
    }

    public AuthorityFailureException(String message) {
        super(message);
    }
}