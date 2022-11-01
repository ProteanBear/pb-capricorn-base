package xyz.proteanbear.capricorn.infrastructure.util;

import java.util.Optional;

/**
 * Tools:数据脱敏工具
 *
 * @author 马强
 */
public class DesensitizedUtil {
    /**
     * 身份证号脱敏
     */
    public static String idNumber(String content) {
        return Optional.ofNullable(content)
                .map(str -> str.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"))
                .orElse("");
    }

    /**
     * 手机号码脱敏
     */
    public static String phoneNumber(String content) {
        return Optional.ofNullable(content)
                .map(str -> str.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"))
                .orElse("");
    }
}
