package xyz.proteanbear.capricorn.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 单元测试：人员身份证工具
 *
 * @author 马强
 */
public class PersonnelIdCardUtilTest {

    /**
     * 测试身份证号校验：长度不足
     */
    @Test
    public void testValidateLengthError(){
        String number="123";
        Assertions.assertFalse(PersonnelIdCardUtil.validate(number));
    }

    /**
     * 测试身份证号校验：格式错误
     */
    @Test
    public void testValidatePatternError(){
        String number="120345AB3434649873";
        Assertions.assertFalse(PersonnelIdCardUtil.validate(number));
    }

    /**
     * 测试身份证号校验：生日超出范围
     */
    @Test
    public void testValidateBirthdayOver(){
        String number="120345187405212400";
        Assertions.assertFalse(PersonnelIdCardUtil.validate(number));
        number="120345257405212400";
        Assertions.assertFalse(PersonnelIdCardUtil.validate(number));
    }

    /**
     * 测试身份证号校验：校验码错误
     */
    @Test
    public void testValidateVerifyCodeError(){
        String number="510109198312042451";
        Assertions.assertFalse(PersonnelIdCardUtil.validate(number));
    }

    /**
     * 测试身份证号校验
     */
    @Test
    public void testValidate15bit(){
        String number="510107820523241";
        Assertions.assertTrue(PersonnelIdCardUtil.validate(number));
    }

    /**
     * 测试身份证号校验
     */
    @Test
    public void testValidate18bit(){
        String number="220802198206202411";
        Assertions.assertTrue(PersonnelIdCardUtil.validate(number));
    }

    /**
     * 测试信息获取工具：错误号码
     */
    @Test
    public void testInfoOfError(){
        String number="120345AB3434649873";
        Assertions.assertTrue(PersonnelIdCardUtil.infoOf(number).isEmpty());
    }

    /**
     * 测试信息获取工具
     */
    @Test
    public void testInfoOf(){
        String number="220802198206202411";
        Optional<PersonnelIdCardUtil.IdCardInfo> info=PersonnelIdCardUtil.infoOf(number);
        Assertions.assertTrue(info.isPresent());
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Assertions.assertEquals(info.get().birthday().format(formatter),"1982-06-20");
        Assertions.assertEquals(info.get().gender(),1);

        try {
            ObjectMapper objectMapper=new ObjectMapper();
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(info.get()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}