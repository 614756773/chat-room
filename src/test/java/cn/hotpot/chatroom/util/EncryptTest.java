package cn.hotpot.chatroom.util;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author qinzhu
 * @since 2020/2/25
 */
public class EncryptTest {
    public static void main(String[] args) {
        //加密所需的salt(盐) 默认是使用我的企鹅号
        String salt = System.getProperty("jasypt.encryptor.password");
        String ciphertext = encrypted(salt, "hotpot");
        System.out.println("加密后的数据：" + ciphertext);

        String original = decrypt(salt, ciphertext);
        System.out.println("解密后的数据" + original);
    }

    private static String decrypt(String salt, String ciphertext) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(salt);
        return encryptor.decrypt(ciphertext);
    }

    private static String encrypted(String salt, String target) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(salt);
        return textEncryptor.encrypt(target);
    }
}
