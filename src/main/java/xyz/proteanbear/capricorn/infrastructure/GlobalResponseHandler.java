package xyz.proteanbear.capricorn.infrastructure;

import xyz.proteanbear.capricorn.infrastructure.auth.AccountAuthorityVerifier;
import xyz.proteanbear.capricorn.infrastructure.auth.Authority;
import xyz.proteanbear.capricorn.infrastructure.util.ClassAndObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * <p>Global return processor, return to unified JSON data structure after capture.</p>
 * <p>子项目可通过继承此类并增加ControllerAdvice注解实现通用的返回处理</p>
 *
 * @author 马强
 */
public abstract class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    /**
     * Record log
     */
    private final Logger logger = LoggerFactory.getLogger(GlobalResponseHandler.class);

    /**
     * Jackson
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Response wrapper：If empty, use the default implementation.
     */
    @Autowired
    private ResponseWrapper responseWrapper;

    /**
     * 继承类实现此方法，注入账户处理器实现类
     */
    public abstract Authority.AccountHandler getAccountHandler();

    /**
     * Turn on support
     *
     * @param methodParameter the method parameter
     * @param aClass          Message conversion class
     * @return Does it support @ResponseBody capture
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * Wrap the result before writing the response body
     *
     * @param data               return data
     * @param methodParameter    method parameter
     * @param mediaType          return media type
     * @param aClass             Message conversion class
     * @param serverHttpRequest  http request
     * @param serverHttpResponse http response
     * @return To write the output
     */
    @Override
    public Object beforeBodyWrite(
            Object data,
            MethodParameter methodParameter,
            MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass,
            ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {

        //Store the data for the method with authority annotation AutoStore
        Class<?> methodClass = methodParameter.getContainingClass();
        Authority.Set classAnnotation = methodClass.getAnnotation(Authority.Set.class);
        if (methodParameter.hasMethodAnnotation(Authority.Set.class) || classAnnotation != null) {
            Authority.Set authoritySetting = methodParameter.getMethodAnnotation(Authority.Set.class);
            authoritySetting = (authoritySetting == null ? classAnnotation : authoritySetting);
            HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            HttpServletResponse response = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();

            //Store the token and account object
            Authority.AccountHandler accountHandler = getAccountHandler();
            if (authoritySetting != null && authoritySetting.autoStore()) {
                if (accountHandler == null) {
                    logger.error("Account handler object is null!");
                } else if (!ClassAndObjectUtil.isImplement(data, Authority.Account.class)) {
                    logger.warn("Return data is not a instance of the class Authority.Account!Data is not stored!");
                } else {
                    Object loginAttribute = request.getAttribute(AccountAuthorityVerifier.ATTRIBUTE_IS_LOGIN);
                    if (loginAttribute==null|| !Boolean.parseBoolean(loginAttribute.toString())) {
                        accountHandler.store(response, (Authority.Account) data);
                    }
                }
            }

            //Remove the token and account object
            if (authoritySetting != null && authoritySetting.autoRemove()) {
                Objects.requireNonNull(accountHandler).remove(request, response);
            }
        }

        //Has not been packaged
        responseWrapper = (responseWrapper == null)
                ? (new ResponseWrapper.Default())
                : responseWrapper;
        if (!(responseWrapper.isObjectAfterWrap(data))) {
            data = responseWrapper.wrap("SUCCESS", data);
        }

        //Debug log
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "Return content:{}",
                        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data)
                );
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }

        return data;
    }
}