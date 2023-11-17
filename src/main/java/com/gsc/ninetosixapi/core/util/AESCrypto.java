package com.gsc.ninetosixapi.core.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Slf4j
public class AESCrypto {
    // private String iv;
    private Key keySpec;
    public static String alg = "AES/CBC/PKCS5Padding";

    public AESCrypto(final String key, final int keySize) throws Exception {
        byte[] keyBytes = null;
        byte[] b = key.getBytes(StandardCharsets.UTF_8);
        String strCipher = null;

        switch (keySize) {
            case 16 -> {
                strCipher = "AES-128";
                keyBytes = new byte[keySize];
            }
            case 24 -> {
                strCipher = "AES-192";
                keyBytes = new byte[keySize];
            }
            case 32 -> {
                strCipher = "AES-256";
                keyBytes = new byte[keySize];
            }
        }
        if(keyBytes == null)
            throw new Exception("암호화 방식이 올바르지 않습니다.");

        int inputKeyLength = key.length();
        if(inputKeyLength > keySize) {
            // 입력받은 key string 길이가 실제 암호화 할 대상 key 길이보다 큰 경우, 암호화 키 길이에 맞게 조정
            System.arraycopy(b, 0, keyBytes, 0, keySize);
        } else if(inputKeyLength < keySize) {
            throw new Exception("Key 길이가 올바르지 않습니다.");
        } else {
            // 입력받은 key string 길이가, 실제 암호화 할 대상 key 길이와 동일한 경우
            System.arraycopy(b, 0, keyBytes, 0, keySize);
        }

        // AES 암호화는 IV 값으로 16 byte 사용
        // iv = key.substring(0, 16);

        // KeySpec 생성 시 입력하는 키 길이에 따라 AES-128, 192, 256 방식으로 자동 설정 됨
        keySpec = new SecretKeySpec(keyBytes, "AES");
    }

    /*public String aesEncode(String text) throws Exception {
        System.out.println("INPUT text: " + text);

        Cipher cipher = Cipher.getInstance(alg);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
        byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }*/

    public String aesDecode(String cipherText, String iv) throws Exception {
        try {
            byte[] ivBytes = convertIV(iv);
            Cipher cipher = Cipher.getInstance(alg);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.getStackTrace();
            log.error(e.getMessage());
        }
        return "";
    }

    public static byte[] convertIV(String base64IV) {
        return Base64.getDecoder().decode(base64IV);
    }

}
