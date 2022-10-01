package xyz.proteanbear.capricorn.infrastructure.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * 单元测试：枚举工具测试
 */
public class EnumsUtilTest {
    /**
     * 测试生成枚举类型字典
     */
    @Test
    public void testDictionary() {
        Map<String, TestEnum> map = EnumsUtil.dictionary(
                TestEnum.values(),
                TestEnum::getKey
        );
        Assertions.assertEquals(map.size(),3);
        Assertions.assertTrue(map.containsKey("A"));

        StringBuilder builder=new StringBuilder("{\n");
        map.forEach((key,value)->builder.append(key).append(" -> ").append(value).append(",\n"));
        builder.append("}\n");
        System.out.println(builder);
    }

    /**
     * 测试获取枚举，不存在时抛出异常
     */
    @Test
    public void testValueOfNotExistThrow(){
        Assertions.assertThrows(EnumsUtil.EnumsValueNotExistException.class,()->
                EnumsUtil.valueOf("V",TestEnum.values(),TestEnum::getKey)
        );
    }

    /**
     * 测试获取枚举，不存在时默认值
     */
    @Test
    public void testValueOfNotExistDefault(){
        Assertions.assertEquals(
                EnumsUtil.valueOf("V",TestEnum.values(),TestEnum::getKey,TestEnum.T),
                TestEnum.T
        );
    }

    /**
     * 测试获取枚举
     */
    @Test
    public void testValueOf(){
        Assertions.assertDoesNotThrow(()->EnumsUtil.valueOf("A",TestEnum.values(),TestEnum::getKey));
        Assertions.assertEquals(EnumsUtil.valueOf("A",TestEnum.values(),TestEnum::getKey,TestEnum.T),TestEnum.A);
    }

    /**
     * 测试枚举类
     */
    public static enum TestEnum {
        A("A", "test A"),
        B("B", "test B"),
        T("T", "default");
        private final String key;
        private final String value;

        TestEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}