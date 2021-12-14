package com.example.commontool.utils.encrypt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * @ClassName: CommonMd5Util
 * @Description: MD5加密工具
 * @Author: th_legend
 **/
@Slf4j
public class CommonEncryptUtil {

    /**
     * 加密方式
     */
    public static String KEY_ALGORITHM = "AES";
    /**
     * 数据填充方式
     */
    public static String ALGORITHM_STR = "AES/CBC/PKCS7Padding";

    /**
     * 只在第一次调用decrypt()方法时才new 对象
     */
    public static boolean initialized = false;

    private CommonEncryptUtil(){}

    /***
     * MD5加码 生成32位md5码
     */
    public static String md5Encrypt(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            log.error("CommonMd5Util::encrypt()::Error::{}",e);
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * sha1加密操作
     *
     * @param content 待加密内容
     * @return 返回String
     */
    public static String sha1Encrypt(String content) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(content.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(String.format("%02X", 0xFF & messageDigest[i]));
            }
            return hexString.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * sha256加密操作
     *
     * @param content 待加密内容
     * @return 返回String
     */
    public static String sha256Encrypt(String content) {
        return SecureUtil.sha256(content);
    }


    /**
     * AES加密
     * 填充模式AES/CBC/PKCS7Padding
     * 解密模式128
     *
     * @param originalContent 加密内容
     * @param encryptKey      加密key
     * @param ivStr           iv偏移量
     * @return
     */
    public static String aesEncrypt(String originalContent, String encryptKey, String ivStr) {
        if (StrUtil.isBlank(originalContent)) {
            return null;
        }
        // 初始化
        initialize();
        try {
            byte[] ivByte = ivStr.getBytes(StandardCharsets.UTF_8);
            byte[] encryptKeyByte = encryptKey.getBytes(StandardCharsets.UTF_8);
            byte[] originalContentByte = originalContent.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
            SecretKeySpec skeySpec = new SecretKeySpec(encryptKeyByte, KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(ivByte));
            byte[] encrypted = cipher.doFinal(originalContentByte);
            return Base64.encode(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES解密
     * 填充模式AES/CBC/PKCS7Padding
     * 解密模式128
     *
     * @param content    目标密文
     * @param decryptKey 解密key
     * @param ivStr      便宜量
     * @return
     */
    public static String aesDecrypt(String content, String decryptKey, String ivStr) {
        if (StrUtil.isBlank(content)) {
            return null;
        }
        initialize();
        try {
            byte[] ivByte = ivStr.getBytes(StandardCharsets.UTF_8);
            byte[] encryptKeyByte = decryptKey.getBytes(StandardCharsets.UTF_8);
            byte[] contentByte = Base64.decode(content);
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
            Key sKeySpec = new SecretKeySpec(encryptKeyByte, KEY_ALGORITHM);
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));
            byte[] result = cipher.doFinal(contentByte);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BouncyCastle作为安全提供，防止我们加密解密时候因为jdk内置的不支持改模式运行报错。
     * 避免重复new生成多个BouncyCastleProvider对象，因为GC回收不了，会造成内存溢出,
     **/
    public static void initialize() {
        if (initialized) {
            return;
        }
        synchronized (CommonEncryptUtil.class){
            if (initialized) {
                return;
            }
            Security.addProvider(new BouncyCastleProvider());
            initialized = true;
        }
    }

    /**
     * 生成iv
     */
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        params.init(new IvParameterSpec(iv));
        return params;
    }


//    public static String aesEncrypt(String content) {
//        return SecureUtil.aes(content);
//    }

    public static void main(String[] args) {
        String s="15454efeafafaf";
        String key="1234567890123456sss";
        String iv="1234567890123456";

    }

}
