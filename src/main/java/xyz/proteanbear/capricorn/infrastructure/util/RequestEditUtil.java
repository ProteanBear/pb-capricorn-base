package xyz.proteanbear.capricorn.infrastructure.util;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Using Reflection to Modify Parameters in HttpRequest（Only can use in Tomcat）.
 *
 * @author 马强
 */
public class RequestEditUtil {
    /**
     * HttpRequest implementation class private requestField in RequestFacade request
     */
    private static Field requestField;

    /**
     * HttpRequest implementation class private parametersParsedField in RequestFacade's type
     */
    private static Field parametersParsedField;

    /**
     * HttpRequest implementation class private coyoteRequestField in RequestFacade's type
     */
    private static Field coyoteRequestField;

    /**
     * HttpRequest implementation class private parametersField in RequestFacade's type
     */
    private static Field parametersField;

    /**
     * HttpRequest implementation class private paramHashValues in RequestFacade's type
     */
    private static Field paramHashValues;

    /**
     * static get has error
     */
    private static boolean error;

    //Get properties through reflection
    static {
        try {
            Class<?> clazz = Class.forName("org.apache.catalina.connector.RequestFacade");
            requestField = clazz.getDeclaredField("request");
            requestField.setAccessible(true);

            parametersParsedField = requestField.getType().getDeclaredField("parametersParsed");
            parametersParsedField.setAccessible(true);

            coyoteRequestField = requestField.getType().getDeclaredField("coyoteRequest");
            coyoteRequestField.setAccessible(true);

            parametersField = coyoteRequestField.getType().getDeclaredField("parameters");
            parametersField.setAccessible(true);

            paramHashValues = parametersField.getType().getDeclaredField("paramHashValues");
            paramHashValues.setAccessible(true);

            error = false;
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
            error = true;
        }
    }

    /**
     * Get a map can edit from the request
     *
     * @param request request
     * @return a parameter map can edit
     */
    private static Map<String, List<String>> getRequestMapCanEdit(ServletRequest request) {
        try {
            if (error) return null;

            Object innerRequest = requestField.get(request);
            parametersParsedField.setBoolean(innerRequest, true);
            Object coyoteRequestObject = coyoteRequestField.get(innerRequest);
            Object parameterObject = parametersField.get(coyoteRequestObject);
            return (Map<String, List<String>>) paramHashValues.get(parameterObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Write the contents of the map to the request
     *
     * @param parameters  parameter content
     * @param intoRequest request
     */
    public static void put(Map<String, List<String>> parameters, ServletRequest intoRequest) {
        Map<String, List<String>> paramMap = getRequestMapCanEdit(intoRequest);
        if (paramMap != null && parameters != null) {
            parameters.forEach(paramMap::put);
        }
    }
}