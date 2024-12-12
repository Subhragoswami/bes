package com.continuum.vendor.service.util;

import com.continuum.vendor.service.exceptions.VendorException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class EncryptionUtil {

    private static final String secretUniqueKey = "continuum";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "AES";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    private static final SecretKey SECRET_KEY = generateSecretKey(secretUniqueKey);
    private static final Map<String, String> DECRYPTION_CACHE = new ConcurrentHashMap<>();

    public static String decryptEmailMobile(String encryptedText) {
        return DECRYPTION_CACHE.computeIfAbsent(encryptedText, EncryptionUtil::decryptionForEmailMobile);
    }

    public static String encrypt(String plainText) {
        try {

            SecretKey secretKey = generateSecretKey(secretUniqueKey);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] ivBytes = new byte[16]; // Initialization Vector (IV) should be random
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] combinedBytes = new byte[ivBytes.length + encryptedBytes.length];

            System.arraycopy(ivBytes, 0, combinedBytes, 0, ivBytes.length);
            System.arraycopy(encryptedBytes, 0, combinedBytes, ivBytes.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combinedBytes);
        } catch (Exception e) {
            throw new VendorException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            SecretKey secretKey = generateSecretKey(secretUniqueKey);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] encryptedBytesWithIV = Base64.getDecoder().decode(encryptedText);
            byte[] ivBytes = new byte[16];
            byte[] encryptedBytes = new byte[encryptedBytesWithIV.length - ivBytes.length];

            System.arraycopy(encryptedBytesWithIV, 0, ivBytes, 0, ivBytes.length);
            System.arraycopy(encryptedBytesWithIV, ivBytes.length, encryptedBytes, 0, encryptedBytes.length);

            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new VendorException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE);
        }
    }

    //Custom encryption for email and mobile.
    public static String encryptionForEmailMobile(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] ivBytes = new byte[16];
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] combinedBytes = new byte[ivBytes.length + encryptedBytes.length];

            System.arraycopy(ivBytes, 0, combinedBytes, 0, ivBytes.length);
            System.arraycopy(encryptedBytes, 0, combinedBytes, ivBytes.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combinedBytes);
        } catch (Exception e) {
            throw new VendorException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE);
        }
    }

    //Custom decryption for email and mobile.
    public static String decryptionForEmailMobile(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] encryptedBytesWithIV = Base64.getDecoder().decode(encryptedText);
            byte[] ivBytes = new byte[16];
            byte[] encryptedBytes = new byte[encryptedBytesWithIV.length - ivBytes.length];

            System.arraycopy(encryptedBytesWithIV, 0, ivBytes, 0, ivBytes.length);
            System.arraycopy(encryptedBytesWithIV, ivBytes.length, encryptedBytes, 0, encryptedBytes.length);

            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, ivSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new VendorException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE);
        }
    }

    private static SecretKey generateSecretKey(String password) {
        try {
            log.info("Generating secret key.");
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), password.getBytes(), ITERATION_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_FACTORY_ALGORITHM);
        } catch (Exception e) {
            log.error("Error while generating secret key {}", e.getMessage());
            throw new VendorException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE);
        }
    }

    public static Map<String, List<String>> bulkDecryptParallel(List<String> encryptedValues) {
        Map<String, List<String>> result = new ConcurrentHashMap<>();

        if (encryptedValues != null) {
            encryptedValues.parallelStream().forEach(value -> {
                if (value != null) {
                    String decryptedValue = decryptEmailMobile(value);

                    if (decryptedValue != null) {
                        result.computeIfAbsent(value, k -> new CopyOnWriteArrayList<>()).add(decryptedValue);
                    }
                }
            });
        }

        return result;
    }



}
