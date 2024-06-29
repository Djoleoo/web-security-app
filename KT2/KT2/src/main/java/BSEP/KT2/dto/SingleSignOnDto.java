package BSEP.KT2.dto;

public class SingleSignOnDto {
    String token;

    public SingleSignOnDto() {
    }

    public SingleSignOnDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
