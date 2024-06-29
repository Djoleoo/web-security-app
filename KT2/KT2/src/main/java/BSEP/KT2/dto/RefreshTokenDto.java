package BSEP.KT2.dto;

public class RefreshTokenDto {
    private String refreshToken;

    public RefreshTokenDto() {
        // Default constructor
    }

    public RefreshTokenDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

