package BSEP.KT2.dto;

public class LoginTokensDto {
    private String accessToken;

    private String refreshToken;

    public LoginTokensDto() {}

    public LoginTokensDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() { return this.accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return this.refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
