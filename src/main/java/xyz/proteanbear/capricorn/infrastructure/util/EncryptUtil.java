package xyz.proteanbear.capricorn.infrastructure.util;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * Tools:加密工具
 *
 * @author 马强
 */
public class EncryptUtil {
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    /**
     * SM2算法名称
     */
    private static final String CRYPTO_NAME_SM2 = "sm2p256v1";

    /*
      静态代码块：加入SM4的加解密实现
     */
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

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
            for (byte bin : bytes) {
                int result = bin & 0xFF;
                if (result < 0x10) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(result));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * SM2生成密钥对
     *
     * @param compressed 是否压缩
     */
    public static SM2Keys generateKeysBySM2(boolean compressed) {
        try {
            X9ECParameters x9ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
            ECDomainParameters domainParameters = new ECDomainParameters(
                    x9ECParameters.getCurve(),
                    x9ECParameters.getG(),
                    x9ECParameters.getN()
            );
            ECKeyPairGenerator generator = new ECKeyPairGenerator();
            generator.init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
            AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();
            return new SM2Keys(
                    Hex.toHexString(((ECPublicKeyParameters) keyPair.getPublic()).getQ().getEncoded(compressed)),
                    ((ECPrivateKeyParameters) keyPair.getPrivate()).getD().toString(16)
            );
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 使用SM2算法进行加密
     *
     * @param publicKey 加密公钥
     * @param str       加密目标内容
     */
    public static String encryptBySM2(String publicKey, String str) {
        //公钥字符串转换为公钥参数
        X9ECParameters x9ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(
                x9ECParameters.getCurve().decodePoint(Hex.decode(publicKey)),
                new ECDomainParameters(
                        x9ECParameters.getCurve(),
                        x9ECParameters.getG(),
                        x9ECParameters.getN()
                )
        );

        //加密
        SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        sm2Engine.init(true, new ParametersWithRandom(publicKeyParameters, new SecureRandom()));
        byte[] strData = str.getBytes(StandardCharsets.UTF_8);
        try {
            return Hex.toHexString(sm2Engine.processBlock(strData, 0, strData.length));
        } catch (InvalidCipherTextException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用SM2算法进行解密
     *
     * @param privateKey   解密私钥
     * @param encryptedStr 已加密的字符串内容
     */
    public static String decryptBySM2(String privateKey, String encryptedStr) {
        //转换私钥字符串为私钥参数
        X9ECParameters x9ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(
                new BigInteger(privateKey, 16),
                new ECDomainParameters(
                        x9ECParameters.getCurve(),
                        x9ECParameters.getG(),
                        x9ECParameters.getN()
                )
        );

        //解密
        SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        sm2Engine.init(false, privateKeyParameters);
        byte[] encrypted = Hex.decode(encryptedStr);
        try {
            return new String(sm2Engine.processBlock(encrypted, 0, encrypted.length));
        } catch (InvalidCipherTextException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用SM2算法进行签名
     *
     * @param privateKey 私钥
     * @param withId     标识ID
     * @param str        签名内容
     */
    public static String signBySM2(String privateKey, String withId, String str) {
        //转换私钥字符串为私钥参数
        X9ECParameters x9ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(
                new BigInteger(privateKey, 16),
                new ECDomainParameters(
                        x9ECParameters.getCurve(),
                        x9ECParameters.getG(),
                        x9ECParameters.getN()
                )
        );
        //签名
        SM2Signer signer = new SM2Signer();
        signer.init(true,
                withId == null || withId.isBlank()
                        ? new ParametersWithRandom(privateKeyParameters, new SecureRandom())
                        : new ParametersWithID(privateKeyParameters, withId.getBytes(StandardCharsets.UTF_8))
        );
        byte[] strData = str.getBytes(StandardCharsets.UTF_8);
        signer.update(strData, 0, strData.length);
        try {
            return Hex.toHexString(signer.generateSignature());
        } catch (CryptoException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用SM2算法进行签名验证
     *
     * @param publicKey 公钥
     * @param withId    标识ID
     * @param str       签名内容
     * @param sign      签名
     */
    public static boolean signBySM2(String publicKey, String withId, String str, String sign) {
        //转换私钥字符串为私钥参数
        X9ECParameters x9ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(
                x9ECParameters.getCurve().decodePoint(Hex.decode(publicKey)),
                new ECDomainParameters(
                        x9ECParameters.getCurve(),
                        x9ECParameters.getG(),
                        x9ECParameters.getN()
                )
        );
        //签名
        SM2Signer signer = new SM2Signer();
        signer.init(false,
                withId == null || withId.isBlank()
                        ? new ParametersWithRandom(publicKeyParameters, new SecureRandom())
                        : new ParametersWithID(publicKeyParameters, withId.getBytes(StandardCharsets.UTF_8))
        );
        byte[] strData = str.getBytes(StandardCharsets.UTF_8);
        signer.update(strData, 0, strData.length);
        return signer.verifySignature(sign.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 使用SM4算法进行加密
     *
     * @param hexKey 16进制字符串加密密钥
     * @param str    加密目标内容
     */
    public static String encryptBySM4(String hexKey, String str) {
        try {
            String name = "SM4";
            byte[] keyData = Hex.decode(hexKey);
            byte[] strData = str.getBytes(StandardCharsets.UTF_8);

            Cipher cipher = Cipher.getInstance(name, BouncyCastleProvider.PROVIDER_NAME);
            Key sm4Key = new SecretKeySpec(keyData, name);
            cipher.init(Cipher.ENCRYPT_MODE, sm4Key);
            byte[] result = cipher.doFinal(strData);
            return Hex.toHexString(result);

        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException |
                 NoSuchProviderException | NoSuchPaddingException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用SM4算法进行解密
     *
     * @param hexKey       16进制字符串加密密钥
     * @param encryptedStr 已加密内容
     */
    public static String decryptBySM4(String hexKey, String encryptedStr) {
        try {
            String name = "SM4";
            byte[] keyData = Hex.decode(hexKey);
            byte[] strData = Hex.decode(encryptedStr);

            Cipher cipher = Cipher.getInstance(name, BouncyCastleProvider.PROVIDER_NAME);
            Key sm4Key = new SecretKeySpec(keyData, name);
            cipher.init(Cipher.DECRYPT_MODE, sm4Key);
            byte[] result = cipher.doFinal(strData);
            return new String(result, StandardCharsets.UTF_8);

        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException |
                 NoSuchProviderException | NoSuchPaddingException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据：记录SM2密钥对
     */
    public record SM2Keys(String publicKey, String privateKey) {
    }
}