package xyz.proteanbear.capricorn.infrastructure.auth;

/**
 * 密码过期异常
 *
 * @author 马强
 */
public class AuthorityAccountPasswordExpiredException extends Exception {
    public AuthorityAccountPasswordExpiredException() {
        super("用户密码已经过期，请重新设置密码！");
    }

    public AuthorityAccountPasswordExpiredException(String message) {
        super(message);
    }
}