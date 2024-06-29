package BSEP.KT2.dto;

public class LoginTwoFactorAuthenticationDto {
    private String username;

    private String password;

    private String reCaptchaResponse;

    private int code;

    public LoginTwoFactorAuthenticationDto() {}

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

    public String getReCaptchaResponse() { return this.reCaptchaResponse; }
    public void setReCaptchaResponse(String reCaptchaResponse) { this.reCaptchaResponse = reCaptchaResponse; }

    public int getCode() { return this.code; }
    public void setCode(int code) { this.code = code; }
}
