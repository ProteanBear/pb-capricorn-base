package xyz.proteanbear.capricorn.infrastructure.util;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * <p>基础层：人员身份证相关工具</p>
 *
 * @author 马强
 */
public class PersonnelIdCardUtil {
    /**
     * 18位身份证中最后一位校验码
     */
    private final static char[] VERIFY_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 18位身份证中，各个数字的生成校验码时的权值
     */
    private final static int[] VERIFY_CODE_WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 验证给定的身份证号是否正确
     *
     * @param number 身份证号
     * @return 正确时返回true
     */
    public static boolean validate(String number) {
        boolean result;
        assert number != null;
        assert !"".equals(number.trim());
        if (number.length() != 15 && number.length() != 18) return false;

        //修正号码：如果为15位，转换为18位
        number = convertToNewCardNumber(number);
        //初步正则表达式校验
        result = Pattern.matches(
                "^[1-9]\\d{5}\\d{4}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",
                number
        );
        if (!result) return false;

        //验证生日日期范围
        LocalDate birthday = LocalDate.parse(
                number.substring(6, 14),
                DateTimeFormatter.ofPattern("yyyyMMdd")
        );
        result = birthday.isAfter(LocalDate.of(1900, 1, 1))
                && birthday.isBefore(LocalDate.now());
        if (!result) return false;

        //验证校验码
        String verifyCode = String.valueOf(calculateVerifyCode(number));
        result = number.substring(17).equalsIgnoreCase(verifyCode);
        return result;
    }

    /**
     * 解析并返回身份证号对应的身份信息
     *
     * @param number 身份证号
     * @return 返回信息，包括生日、年龄、性别等;如果号码错误，则返回empty
     */
    public static Optional<IdCardInfo> infoOf(String number) {
        assert number != null;
        assert !"".equals(number.trim());
        if (!validate(number)) return Optional.empty();

        //取出生日
        number = convertToNewCardNumber(number);
        LocalDate birthday = LocalDate.parse(
                number.substring(6, 14),
                DateTimeFormatter.ofPattern("yyyyMMdd")
        );
        //计算年龄
        int age = birthday.until(LocalDate.now()).getYears();
        //取出性别
        int gender = Integer.parseInt(number.substring(16, 17)) % 2;

        return Optional.of(new IdCardInfo(birthday, age, gender));
    }

    /**
     * 转换当前的身份证号为统一的18位
     *
     * @param number 身份证号
     * @return 如果已经为18位返回，如果为15位返回转换后的身份证号
     */
    private static String convertToNewCardNumber(String number) {
        assert number != null;
        assert !"".equals(number.trim());

        //已经是18位，直接返回
        int targetLength = 18;
        if (number.length() == targetLength) return number;

        //拼接年份
        StringBuilder buf = new StringBuilder(targetLength);
        buf.append(number, 0, 6).append("19").append(number.substring(6));

        //计算校验位
        buf.append(calculateVerifyCode(buf));
        return buf.toString();
    }

    /**
     * 计算最后的校验位
     *
     * @param cardNumber 身份证号
     * @return 最后一位校验位
     */
    private static char calculateVerifyCode(CharSequence cardNumber) {
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (cardNumber.charAt(i) - '0') * VERIFY_CODE_WEIGHT[i];
        }
        return VERIFY_CODE[sum % 11];
    }

    /**
     * 身份证内的相关信息
     */
    public static class IdCardInfo implements Serializable {
        /**
         * 生日
         */
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        private final LocalDate birthday;

        /**
         * 年龄
         */
        private final int age;

        /**
         * 性别，0-女、1-男
         */
        private final int gender;

        public IdCardInfo(LocalDate birthday, int age, int gender) {
            this.birthday = birthday;
            this.age = age;
            this.gender = gender;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public int getAge() {
            return age;
        }

        public int getGender() {
            return gender;
        }
    }
}