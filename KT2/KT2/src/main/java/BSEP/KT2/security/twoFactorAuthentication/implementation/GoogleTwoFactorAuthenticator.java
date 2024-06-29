package BSEP.KT2.security.twoFactorAuthentication.implementation;

import org.springframework.stereotype.Component;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import BSEP.KT2.security.twoFactorAuthentication.IGoogleTwoFactorAuthenticator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleTwoFactorAuthenticator implements IGoogleTwoFactorAuthenticator {
    private final GoogleAuthenticator authenticator;

    @Override
    public GoogleAuthenticatorKey createCredentials(String identity) {
        GoogleAuthenticatorKey key = authenticator.createCredentials(identity);
        return key;
    }

    @Override
    public String getOtpAuthTotpURL(String issuer, String identity, GoogleAuthenticatorKey credentials) {
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer, identity, credentials);
        return otpAuthURL;
    }

    @Override
    public boolean authenticate(String identity, int code) {
        boolean isAuthenticated = authenticator.authorizeUser(identity, code);
        return isAuthenticated;
    }
}
