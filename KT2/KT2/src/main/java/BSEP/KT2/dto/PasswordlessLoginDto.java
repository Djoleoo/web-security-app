package BSEP.KT2.dto;

public class PasswordlessLoginDto {
    private String token;

    private String signature;

    private String reCaptchaResponse;

    public PasswordlessLoginDto() {}

    public String getToken() { return this.token; }
    public void setToken(String token) { this.token = token; }

    public String getSignature() { return this.signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public String getReCaptchaResponse() { return this.reCaptchaResponse; }
    public void setReCaptchaResponse(String reCaptchaResponse) { this.reCaptchaResponse = reCaptchaResponse; }
}
