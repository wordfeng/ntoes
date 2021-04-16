package org.feng.security.algorithm;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.stream.Collectors;

public class RSA {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final RSAPrivateKey privateKey;
    private static final RSAPublicKey publicKey;

    static {
        KeyPair keyPair = null;
        try {
            keyPair = genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert keyPair != null;
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        publicKey = (RSAPublicKey) keyPair.getPublic();
//        System.out.println("初始化公钥：" + new String(publicKey.getEncoded()));
//        System.out.println("初始化私钥：" + new String(privateKey.getEncoded()));
    }

    public static void main(String[] args) throws Exception {

        String src = new BufferedReader(
                new InputStreamReader(new URL("https://api.lovelive.tools/api/SweetNothings/:count/Serialization/:serializationType").openConnection().getInputStream(), "UTF-8"))
                .lines()
                .collect(Collectors.joining("\n")).replaceAll("\\{\"code\":200,\"message\":\"\",\"returnObj\":\\[\"", "")
                .replaceAll("\"]}", "");

        byte[] encryptByPublicKey = encryptByPublicKey(src, getEncodedPublicKey());
        String encrypted = new String(encryptByPublicKey);
        System.out.println("encryptByPublicKey: " + encrypted);

        String decryptByPrivate = new String(decryptByPrivateKey(encryptByPublicKey, getEncodedPrivateKey()));
        System.out.println("decryptByPrivate: " + decryptByPrivate);

        byte[] encryptByPrivateKey = encryptByPrivateKey(src, getEncodedPrivateKey());
        System.out.println("encryptByPrivateKey: " + new String(encryptByPrivateKey));
        byte[] decryptByPublicKey = decryptByPublicKey(encryptByPrivateKey, getEncodedPublicKey());
        System.out.println("decryptByPublicKey: " + new String(decryptByPublicKey));

        byte[] signed = sign(src, getEncodedPrivateKey());
        System.out.println("signed: " + new String(signed));
        boolean verify = verify(src, getEncodedPublicKey(), signed);
        System.out.println("verify: " + verify);
        signed[1] = 2;
        System.out.println(verify(src, getEncodedPublicKey(), signed));

    }

    public static KeyPair genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        return keyPairGen.generateKeyPair();
    }

    public static byte[] sign(String data, byte[] privateKey) throws Exception {
        return sign(data.getBytes(), privateKey);
    }

    /**
     * 生成数字签名
     *
     * @param data       已加密
     * @param privateKey 私钥
     */
    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verify(String data, byte[] publicKey, byte[] signed)
            throws Exception {
        return verify(data.getBytes(), publicKey, signed);
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密
     * @param publicKey 公钥
     * @param signed    已签名
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] signed)
            throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(signed);
    }

    /**
     * 私钥解密
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, byte[] privateKey)
            throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, byte[] publicKey)
            throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }


    public static byte[] encryptByPublicKey(String data, byte[] publicKey) throws Exception {
        return encryptByPublicKey(data.getBytes(), publicKey);
    }

    /**
     * 公钥加密
     *
     * @param data 源数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static byte[] encryptByPrivateKey(String data, byte[] privateKey)
            throws Exception {
        return encryptByPrivateKey(data.getBytes(), privateKey);
    }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey)
            throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public static RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public static byte[] getEncodedPrivateKey() {
        return privateKey.getEncoded();
    }

    public static byte[] getEncodedPublicKey() {
        return publicKey.getEncoded();
    }
}
