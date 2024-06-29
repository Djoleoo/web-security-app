package BSEP.KT2.security.encryption.implementation;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import BSEP.KT2.security.encryption.IEncryptor;

@Component
public class Encryptor implements IEncryptor{
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    @Value("${application.encryption.secret-key}") private String ENCRYPTION_SECRET_KEY;

    private static SecretKey generateKey(byte[] keyValue) {
        return new SecretKeySpec(keyValue, ALGORITHM);
    }

    public String encrypt(String entry) {
        try {
            SecretKey secretKey = generateKey(ENCRYPTION_SECRET_KEY.getBytes());
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(entry.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String decrypt(String entry) {
        try {
            SecretKey secretKey = generateKey(ENCRYPTION_SECRET_KEY.getBytes());
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(entry));
            return new String(decryptedBytes);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
