package BSEP.KT2.dto;

public class PasswordlessLoginTwoFactorAuthenticationDto {
    private String token;

    private String signature;

    private String reCaptchaResponse;

    private int code;

    public PasswordlessLoginTwoFactorAuthenticationDto() {}

    public String getToken() { return this.token; }
    public void setToken(String token) { this.token = token; }

    public String getSignature() { return this.signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public int getCode() { return this.code; }
    public void setCode(int code) { this.code = code; }

    public String getReCaptchaResponse() { return this.reCaptchaResponse; }
    public void setReCaptchaResponse(String reCaptchaResponse) { this.reCaptchaResponse = reCaptchaResponse; }
}
