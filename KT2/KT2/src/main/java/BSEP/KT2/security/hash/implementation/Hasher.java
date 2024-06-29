package BSEP.KT2.security.hash.implementation;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import BSEP.KT2.security.hash.IHasher;

@Component
public class Hasher implements IHasher{
    @Value("${application.hash.hmac.secret-key}") private String HMAC_SECRET_KEY;

    public String hash(String entry) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(entry.getBytes());
            byte[] hashedBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return entry = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String hashSalted(String entry, String salt) {
        String saltedEntry = entry + salt;

        return hash(saltedEntry);
    }
  
    public String hashHmac(String entry) {
        String algorithm = "HmacSHA256";
        
        try {
            Mac hmac = Mac.getInstance(algorithm);

            SecretKeySpec secretKeySpec = new SecretKeySpec(HMAC_SECRET_KEY.getBytes(), algorithm);

            hmac.init(secretKeySpec);

            byte[] hmacBytes = hmac.doFinal(entry.getBytes());

            String hmacString = Base64.getEncoder().encodeToString(hmacBytes);

            return hmacString;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }
}
