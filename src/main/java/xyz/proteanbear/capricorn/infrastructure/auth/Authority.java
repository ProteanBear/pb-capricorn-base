package xyz.proteanbear.capricorn.infrastructure.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Serializable;
import java.lang.annotation.*;
import java.util.UUID;

/**
 * <p>基础设施：账户权限</p>
 *
 * @author 马强
 */
public class Authority {
    /**
     * <p>权限设置注解，用于Controller的方法上</p>
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    public @interface Set {
        /**
         * 错误信息
         */
        String message() default "用户未登录，或没有此服务接口的使用权限";

        /**
         * 指定当前接口是否必须登录才可使用，默认为true
         */
        boolean mustLogin() default true;

        /**
         * <p>如果设置为true,通过accountClass或accountClassName指定的账户类实现将可以使用此接口</p>
         * <p>如果设置为false, 通过accountClass或accountClassName指定的账户类实现将不可以使用此接口</p>
         */
        boolean allow() default false;

        /**
         * <p>用于用户登录接口，你可以在方法中返回账户类（实现了本类中的Account接口），即可自动存储。</p>
         * <p>需配合GlobalResponseHandler实现。</p>
         */
        boolean autoStore() default false;

        /**
         * <p>用于用户登出接口，你可以在方法中返回账户类（实现了本类中的Account接口），即可自动清除存储。</p>
         * <p>需配合GlobalResponseHandler实现。</p>
         */
        boolean autoRemove() default false;

        /**
         * 不同账号类模式：指定账号实现类
         */
        Class[] accountClass() default {};

        /**
         * 不同账号类模式：指定账户实现类名称
         */
        String[] accountClassName() default {};

        /**
         * 同账号类模式：通过账号在本应用中分配的权限标识进行鉴权
         */
        String accountAuthKey() default "";

        /**
         * 多拦截器模式：指定使用的拦截器类名（拦截器中会判断当前键名，不匹配则跳过）
         */
        String verifierKey() default "";
    }

    /**
     * 账户类接口，默认实现；并可序列化
     */
    public interface Account extends Serializable {
        /**
         * 检查账户配置是否通过鉴权，通过鉴权即可以使用对应的接口
         *
         * @param accountAuthKey 账户鉴权标识
         */
        default boolean ok(String accountAuthKey) {
            return true;
        }

        /**
         * 自定义Token实现方法，默认使用UUID。
         */
        default String customToken() {
            return String.valueOf(UUID.randomUUID()).replaceAll("-", "");
        }

        /**
         * 获取当前账号接口实现类
         */
        @SuppressWarnings("unchecked")
        default <T> T getBean(Class<T> toClass) {
            return this.getClass().isAssignableFrom(toClass) ? ((T) this) : null;
        }

        /**
         * 设置Token
         */
        void setToken(String token);
    }

    /**
     * 账户处理器接口
     */
    public interface AccountHandler {
        /**
         * Get the account object through web request
         *
         * @param request web request
         * @return current account object
         */
        Account get(HttpServletRequest request);

        /**
         * Get the account object through token string
         *
         * @param fromToken token string
         * @param forClass  class name
         * @return current account object
         */
        Account get(String fromToken, String forClass);

        /**
         * @param request web request
         * @return token string
         */
        String token(HttpServletRequest request);

        /**
         * @param request web request
         * @return account object type string
         */
        String type(HttpServletRequest request);

        /**
         * Store the account object
         *
         * @param response response
         * @param account  current account object
         */
        void store(HttpServletResponse response, Account account);

        /**
         * Remove the account object
         *
         * @param request  web request
         * @param response response
         */
        void remove(HttpServletRequest request, HttpServletResponse response);
    }
}