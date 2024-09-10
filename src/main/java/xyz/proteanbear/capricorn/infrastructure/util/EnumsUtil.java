package xyz.proteanbear.capricorn.infrastructure.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>基础层：枚举处理工具</p>
 *
 * @author 马强
 */
public class EnumsUtil {
    /**
     * 从全部枚举值中获取当前值对应的枚举，如果没有则抛出异常
     *
     * @param enums       全部枚举值
     * @param valueGetter 识别值字段获取方法
     * @param <K>         识别值类型
     * @param <E>         枚举值类型
     * @return 值对应的枚举，如果没有则抛出异常
     * @throws IllegalArgumentException IllegalArgumentException
     */
    public static <K, E extends Enum<E>> E valueOf(K value, E[] enums, ValueGetter<K, E> valueGetter) throws EnumsValueNotExistException {
        Map<K, E> dictionary = dictionary(enums, valueGetter);
        if (!dictionary.containsKey(value)) throw new EnumsValueNotExistException();
        return dictionary.get(value);
    }

    /**
     * 从全部枚举值中获取当前值对应的枚举，如果没有则使用默认值
     *
     * @param enums        全部枚举值
     * @param valueGetter  识别值字段获取方法
     * @param defaultValue 默认值
     * @param <K>          识别值类型
     * @param <E>          枚举值类型
     * @return 值对应的枚举，如果没有则使用默认值
     */
    public static <K, E extends Enum<E>> E valueOf(K value, E[] enums, ValueGetter<K, E> valueGetter, E defaultValue) {
        return dictionary(enums, valueGetter).getOrDefault(value, defaultValue);
    }

    /**
     * 获取指定枚举类型的获取字典
     *
     * @param enums       全部枚举值
     * @param valueGetter 识别值字段获取方法
     * @param <K>         识别值类型
     * @param <E>         枚举值类型
     * @return 返回字典内容为识别值对应枚举值
     */
    public static <K, E extends Enum<E>> Map<K, E> dictionary(E[] enums, ValueGetter<K, E> valueGetter) {
        Map<K, E> result = new HashMap<>();
        for (E anEnum : enums) {
            result.put(valueGetter.getValue(anEnum), anEnum);
        }
        return result;
    }

    /**
     * 枚举值获取键名接口
     */
    public interface ValueGetter<K, E extends Enum<E>> {
        /**
         * 返回键名对象
         */
        K getValue(E enumObj);
    }

    /**
     * 枚举值不存在异常
     */
    public static class EnumsValueNotExistException extends Exception {
        public EnumsValueNotExistException() {
            super("Enumeration value does not exist.");
        }
    }
}