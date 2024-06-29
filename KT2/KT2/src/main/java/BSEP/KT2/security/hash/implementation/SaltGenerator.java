package BSEP.KT2.security.hash.implementation;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

import BSEP.KT2.security.hash.ISaltGenerator;

@Component
public class SaltGenerator implements ISaltGenerator{
    public String generate() {
        byte[] salt = new byte[16];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        
        return Base64.getEncoder().encodeToString(salt);
    }
}
