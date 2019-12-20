package cn.hotpot.chartroom.common.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author qinzhu
 * @since 2019/12/19
 */
public class AesEncryptUtils {

    /**
     * 必须是16个字符，即32位（4字节）
     * java中一个char占两位，因为包含中文的编码
     */
    private static final String SALT = "0123456789123456";

    /**
     * 算法名称/加密模式/数据填充方式
     */
    private static final String CONFIG = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     *
     * @param content    加密的字符串
     * @param encryptKey key值
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        Cipher cipher = Cipher.getInstance(CONFIG);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        byte[] b = cipher.doFinal(content.getBytes("utf-8"));
        // 采用base64算法进行转码,避免出现中文乱码
        return Base64.encodeBase64String(b);
    }

    /**
     * 解密
     *
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     */
    private static String decrypt(String encryptStr, String decryptKey) throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        Cipher cipher = Cipher.getInstance(CONFIG);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    public static String encrypt(String content){
        try {
            return encrypt(content, SALT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String encryptStr){
        try {
            return decrypt(encryptStr, SALT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws Exception {
        String content = "123456";
        System.out.println("加密前：" + content);

        String encrypt = encrypt(content, SALT);
        System.out.println("加密后：" + encrypt);

        String decrypt = decrypt(encrypt, SALT);
        System.out.println("解密后：" + decrypt);
    }
}
