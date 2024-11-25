package xyz.proteanbear.capricorn.infrastructure.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 单元测试：Class and object related tools
 */
public class ClassAndObjectUtilTest {
    /**
     * 测试方法：测试一个类是否实现了某个接口
     */
    @Test
    public void testIsImplement() {
        Assertions.assertFalse(ClassAndObjectUtil.isImplement(new StringBuilder(), TestInterface.class));
        Assertions.assertTrue(ClassAndObjectUtil.isImplement(new TestInterfaceClass(), TestInterface.class));
        Assertions.assertFalse(ClassAndObjectUtil.isImplement(StringBuilder.class, TestInterface.class));
        Assertions.assertTrue(ClassAndObjectUtil.isImplement(TestInterfaceClass.class, TestInterface.class));
        Assertions.assertFalse(ClassAndObjectUtil.isImplement(new StringBuilder(), "TestInterface"));
        Assertions.assertTrue(ClassAndObjectUtil.isImplement(new TestInterfaceClass(), "TestInterface"));
        Assertions.assertFalse(ClassAndObjectUtil.isImplement(StringBuilder.class, "TestInterface"));
        Assertions.assertTrue(ClassAndObjectUtil.isImplement(TestInterfaceClass.class, "TestInterface"));
    }

    /**
     * 测试方法：测试一个类是否在某个包内
     */
    @Test
    public void testInPackage(){
        Assertions.assertFalse(ClassAndObjectUtil.inPackage(StringBuilder.class,"xyz.proteanbear"));
        Assertions.assertTrue(ClassAndObjectUtil.inPackage(StringBuilder.class,"java.lang"));
        Assertions.assertFalse(ClassAndObjectUtil.inPackage(ClassAndObjectUtil.class,"xyz.proteanbear.capricorn.infrastructure"));
        Assertions.assertTrue(ClassAndObjectUtil.inPackage(ClassAndObjectUtil.class,"xyz.proteanbear.capricorn.infrastructure.util"));
    }

    /**
     * 测试方法：获取一个类的全部属性字段（包括父类）
     */
    @Test
    public void testGetFields(){
        List<Field> fields=ClassAndObjectUtil.getFields(TestClass.class);
        Assertions.assertFalse(fields.isEmpty());
        Assertions.assertEquals(fields.size(),3);
        fields.forEach(field -> System.out.println(field.getName()));
    }

    interface TestInterface {
        String testFunc();
    }

    public static class TestInterfaceClass implements TestInterface {
        @Override
        public String testFunc() {
            return "";
        }
    }

    public static class TestParent{
        private String a;
        private Integer b;
    }

    public static class TestClass extends TestParent{
        private String c;
    }
}