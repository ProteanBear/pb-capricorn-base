package xyz.proteanbear.capricorn.infrastructure.constant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

/**
 * 单元测试：常用正则表达式
 */
public class RegularExpressionTest {
    /**
     * 测试账号格式
     */
    @Test
    public void testAccount(){
        Assertions.assertFalse(Pattern.matches(RegularExpression.Account,"012"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.Account,"01256"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.Account,"Aca34556"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.Account,"Aca34556f5d6d3f6d3"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.Account,"AC-34556"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.Account,"AC-345_56"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.Account,"AC-345_56%$"));
    }

    /**
     * 测试人员名称（中文）
     */
    @Test
    public void testPersonNameChinese(){
        Assertions.assertFalse(Pattern.matches(RegularExpression.PersonName,"012ask"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.PersonName,"中国chinese"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.PersonName,"中华人员共和国四川省成都市"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.PersonName,"李静"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.PersonName,"唐吉歌德·哥特"));
    }

    /**
     * 测试人员名称（英文）
     */
    @Test
    public void testPersonNameEnglish(){
        Assertions.assertFalse(Pattern.matches(RegularExpression.PersonName,"012ask"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.PersonName,"中国chinese"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.PersonName,"Adolph Blaine Charles David Earl Frederick Gerald Hubert Irvim John Kenneth Loyd Martin Nero Oliver Paul Quincy Randolph"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.PersonName,"Giannis Antetokounmpo"));
    }

    /**
     * 测试密码（必须包含字母和数字，长度6~18位）
     */
    @Test
    public void testPassword(){
        Assertions.assertFalse(Pattern.matches(RegularExpression.Password,"1234"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.Password,"123456"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.Password,"compare"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.Password,"abCdE1234567890feb5"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.Password,"a123456"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.Password,"1A23456"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.Password,"Aa123456&%$"));
    }

    /**
     * 测试日期(yyyy-MM-dd)
     */
    @Test
    public void testDate(){
        Assertions.assertFalse(Pattern.matches(RegularExpression.Date,"1234"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.Date,"1234-13-31"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.Date,"2021-4-5"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.Date,"2021-04-31"));
        Assertions.assertFalse(Pattern.matches(RegularExpression.Date,"2021-02-29"));
        Assertions.assertTrue(Pattern.matches(RegularExpression.Date,"2020-02-29"));
    }
}