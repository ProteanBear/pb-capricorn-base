package xyz.proteanbear.capricorn.infrastructure.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import xyz.proteanbear.capricorn.infrastructure.constant.Constants;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 工具类：处理HTTP头相关的处理
 */
public class HttpHeaderUtil {
    /**
     * 生成文件输出时的返回头
     */
    public static HttpHeaders headers(String fileName){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.DateTimeWithoutSecondPattern)));
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return headers;
    }

    /**
     * 生成文件输出时的返回格式
     */
    public static MediaType contentType(HttpServletRequest request, String filePath){
        return MediaType.parseMediaType(
                Optional.ofNullable(filePath)
                        .map(path -> request.getServletContext().getMimeType(path))
                        .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE)
        );
    }
}