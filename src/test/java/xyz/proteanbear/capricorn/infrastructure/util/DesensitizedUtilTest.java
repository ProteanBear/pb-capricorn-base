package xyz.proteanbear.capricorn.infrastructure.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 单元测试：脱敏工具
 */
public class DesensitizedUtilTest {
    /**
     * 测试：身份证号脱敏
     */
    @Test
    public void testIdNumber(){
        Assertions.assertEquals(DesensitizedUtil.idNumber(null),"");
        Assertions.assertEquals(DesensitizedUtil.idNumber(""),"");
        Assertions.assertEquals(DesensitizedUtil.idNumber("1234567"),"1234567");
        Assertions.assertEquals(DesensitizedUtil.idNumber("510456185608763456"),"510***********3456");
    }

    /**
     * 测试：手机号码脱敏
     */
    @Test
    public void testPhoneNumber(){
        Assertions.assertEquals(DesensitizedUtil.idNumber(null),"");
        Assertions.assertEquals(DesensitizedUtil.idNumber(""),"");
        Assertions.assertEquals(DesensitizedUtil.idNumber("1234567"),"1234567");
        Assertions.assertEquals(DesensitizedUtil.idNumber("13300000000"),"133****0000");
    }
}