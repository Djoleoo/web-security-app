package BSEP.KT2.dto;

public class PasswordlessLoginRequestDto {
    private String username;

    private String reCaptchaResponse;

    public PasswordlessLoginRequestDto() {}

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getReCaptchaResponse() { return this.reCaptchaResponse; }
    public void setReCaptchaResponse(String reCaptchaResponse) { this.reCaptchaResponse = reCaptchaResponse; }
}
