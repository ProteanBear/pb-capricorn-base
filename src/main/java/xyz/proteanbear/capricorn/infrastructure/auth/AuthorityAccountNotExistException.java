package xyz.proteanbear.capricorn.infrastructure.auth;

/**
 * 鉴权失败异常
 *
 * @author 马强
 */
public class AuthorityAccountNotExistException extends Exception {
    public AuthorityAccountNotExistException() {
        super("用户未登录，或者用户登录已过期");
    }

    public AuthorityAccountNotExistException(String message) {
        super(message);
    }
}