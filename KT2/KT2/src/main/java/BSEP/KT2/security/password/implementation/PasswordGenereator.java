package BSEP.KT2.security.password.implementation;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import BSEP.KT2.security.password.IPasswordGenerator;

@Component
public class PasswordGenereator implements IPasswordGenerator{
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = ".";
    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIALS;
    private static final int PASSWORD_LENGTH = 10;

    public String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        
        List<Character> passwordChars = new ArrayList<>();
        
        passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIALS.charAt(random.nextInt(SPECIALS.length())));
        
        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            passwordChars.add(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        Collections.shuffle(passwordChars, random);
        
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (Character ch : passwordChars) {
            password.append(ch);
        }
        
        return password.toString();
    }
}
