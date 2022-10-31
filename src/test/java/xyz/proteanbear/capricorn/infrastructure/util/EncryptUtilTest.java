package xyz.proteanbear.capricorn.infrastructure.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 单元测试：加密工具
 */
public class EncryptUtilTest {
    /**
     * 测试MD5加密
     */
    @Test
    public void testEncryptByMd5() {
        String sourceString = "test";
        String encryptedFirst = EncryptUtil.encryptByMd5(sourceString);
        System.out.println("MD5加密前：" + sourceString);
        System.out.println("MD5加密后：" + encryptedFirst);
        Assertions.assertEquals(EncryptUtil.encryptByMd5(sourceString), encryptedFirst);
    }

    /**
     * 测试SM2解密
     */
    @Test
    public void testEncryptAndDecryptBySM2() {
        String sourceString = "SM2国密加密解密测试";
        EncryptUtil.SM2Keys keys = EncryptUtil.generateKeysBySM2(true);
        System.out.println("SM2生成公钥：" + keys.publicKey());
        System.out.println("SM2生成私钥：" + keys.privateKey());
        String encrypted = EncryptUtil.encryptBySM2(keys.publicKey(), sourceString);
        System.out.println("SM2加密前：" + sourceString);
        System.out.println("SM2加密后：" + encrypted);
        String decrypted = EncryptUtil.decryptBySM2(keys.privateKey(), encrypted);
        System.out.println("SM2解密后：" + decrypted);
        Assertions.assertEquals(sourceString, decrypted);
    }

    /**
     * 测试SM4加解密
     */
    @Test
    public void testEncryptAndDecryptBySM4() {
        String sourceString = "SM4国密加密解密测试";
        String hexKey = EncryptUtil.encryptByMd5(sourceString);
        String encrypted = EncryptUtil.encryptBySM4(hexKey, sourceString);
        System.out.println("SM4加密前：" + sourceString);
        System.out.println("SM4加密后：" + encrypted);
        String decrypted = EncryptUtil.decryptBySM4(hexKey, encrypted);
        System.out.println("SM4解密后：" + decrypted);
        Assertions.assertEquals(sourceString, decrypted);
    }
}
