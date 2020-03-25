package cn.hotpot.chatroom.common.utils;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author qinzhu
 * @since 2019/12/19
 */
public class AesEncryptUtils {
    private final static String ENCODING = "UTF-8";
    private final static byte[] IV = {0x38, 0x37, 0x36, 0x30, 0x34, 0x33,
            0x32, 0x37, 0x38, 0x37, 0x36, 0x35, 0x31, 0x33, 0x32, 0x31};
    private final static String DEFAULT_SALT = "12dc125f000db610cab6bfe4b0dae71c";

    public static synchronized String encrypt(String content) {
        byte[] sendBytes = null;
        try {
            sendBytes = DEFAULT_SALT.getBytes(ENCODING);
            content = URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedBlockCipher engine = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new AESFastEngine()));
        engine
                .init(true, new ParametersWithIV(new KeyParameter(sendBytes),
                        IV));
        byte[] enc = new byte[engine.getOutputSize(content.getBytes().length)];
        int size1 = engine.processBytes(content.getBytes(), 0, content
                .getBytes().length, enc, 0);
        try {
            byte[] encryptedContent = new byte[size1 + engine.doFinal(enc, size1)];
            System.arraycopy(enc, 0, encryptedContent, 0, encryptedContent.length);
            return new String(Hex.encode(encryptedContent));
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static synchronized String decrypt(String cipherText) {
        byte[] sendBytes;
        try {
            sendBytes = DEFAULT_SALT.getBytes(ENCODING);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1);
        }

        byte[] encryptedContent = hex2byte(cipherText);
        BufferedBlockCipher engine = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new AESFastEngine()));

        engine.init(false,
                new ParametersWithIV(new KeyParameter(sendBytes), IV));
        byte[] dec = new byte[engine.getOutputSize(encryptedContent.length)];
        int size1 = engine.processBytes(encryptedContent, 0,
                encryptedContent.length, dec, 0);
        try {
            byte[] decryptedContent = new byte[size1 + engine.doFinal(dec, size1)];
            System.arraycopy(dec, 0, decryptedContent, 0, decryptedContent.length);
            String content = new String(decryptedContent);
            return URLDecoder.decode(content, "utf-8");
        } catch (InvalidCipherTextException | UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    public static void main(String[] args) {

        String content = "测试内容";
        String encryptStr = null;
        try {
            encryptStr = encrypt(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("加密后=" + encryptStr);

        String decryptStr = null;
        try {
            decryptStr = decrypt(encryptStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("解密后=" + decryptStr);
    }
}
