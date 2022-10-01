package xyz.proteanbear.capricorn.infrastructure.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Tools:加密工具
 *
 * @author 马强
 */
public class EncryptUtil {
    /**
     * 使用MD5加密
     *
     * @param str 加密内容
     * @return 加密后内容
     */
    public static String encryptByMd5(String str) {
        try {
            //加密
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update((str).getBytes(StandardCharsets.UTF_8));

            //转换为十六进制字符串
            byte[] bytes = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for(byte bin : bytes) {
                int result = bin & 0xFF;
                if(result < 0x10) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(result));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}