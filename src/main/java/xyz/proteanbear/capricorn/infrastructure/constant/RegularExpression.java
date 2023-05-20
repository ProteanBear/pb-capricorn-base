package xyz.proteanbear.capricorn.infrastructure.constant;

/**
 * <p>常量：常用正则表达式</p>
 *
 * @author 马强
 */
public class RegularExpression {
    public static final String Account="^[a-zA-Z0-9_-]{4,16}$";
    public static final String AccountLarge="^[a-zA-Z0-9_-]{4,32}$";
    public static final String PersonName="^(([\u4E00-\u9FA5]|[a-zA-Z]){1})([\u4E00-\u9FA5·]{0,11}|[a-zA-Z\\s]{0,31})$";
    public static final String Password="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\W]{6,18}$";
    public static final String Date="^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$";
    public static final String Time="^((20|21|22|23|[0-1]\\d):[0-5]\\d)$";
    public static final String DateOrTime="^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)|((20|21|22|23|[0-1]\\d):[0-5]\\d))$";
    public static final String Mobile="^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
    public static final String Telephone="^(0\\d{2,3})-?(\\d{7,8})$";
    public static final String Contact="^((13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8})|(0\\d{2,3})-?(\\d{7,8})$";
    public static final String Email="^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String Amount="^(([0-9]|([1-9][0-9]{0,9}))((\\\\.[0-9]{1,2})?))$";
}