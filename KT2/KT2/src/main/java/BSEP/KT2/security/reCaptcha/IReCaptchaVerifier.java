package BSEP.KT2.security.reCaptcha;

public interface IReCaptchaVerifier {
    public boolean verify(String reCaptchaResponse);
}
