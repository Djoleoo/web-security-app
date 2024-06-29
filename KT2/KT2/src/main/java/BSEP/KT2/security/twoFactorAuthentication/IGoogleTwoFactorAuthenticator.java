package BSEP.KT2.security.twoFactorAuthentication;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public interface IGoogleTwoFactorAuthenticator {
    public GoogleAuthenticatorKey createCredentials(String identity);
    public String getOtpAuthTotpURL(String issuer, String identity, GoogleAuthenticatorKey credentials);
    public boolean authenticate(String identity, int code);
}
