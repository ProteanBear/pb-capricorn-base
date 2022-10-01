package xyz.proteanbear.capricorn.infrastructure;

import org.springframework.data.domain.Page;

/**
 * Return to the content wrapper,
 * wrap the returned data as a custom structure and return.
 *
 * @author 马强
 * @since jdk1.8
 */
public interface ResponseWrapper {
    /**
     * Check if the specified object is a wrapped object.
     *
     * @param object Can not be empty
     * @return If empty, the default is true
     */
    default boolean isObjectAfterWrap(Object object) {
        return (object instanceof ResponseDefault)||(object instanceof PageResponseDefault);
    }

    /**
     * wrap the returned data as a custom structure and return.
     *
     * @param message return description
     * @param data    Arbitrary object
     * @return Wrapped output object
     */
    default Object wrap(String message, Object data) {
        //Pagination Response
        if(data instanceof Page){
            return PageResponseDefault.success(message, (Page<?>) data);
        }

        return new ResponseDefault("SUCCESS", message, data);
    }

    /**
     * wrap the returned data as a custom structure and return.
     *
     * @param status  return status code
     * @param message return description
     * @return Wrapped output object
     */
    default Object wrap(String status, String message) {
        return new ResponseDefault(status, message, null);
    }

    /**
     * Default content wrapper implementation.
     */
    class Default implements ResponseWrapper {
    }
}