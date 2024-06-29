package BSEP.KT2.dto;

public class UserActivationDto {
    private String token;

    private String signature;

    public UserActivationDto() {}

    public String getToken() { return this.token; }
    public void setToken(String token) { this.token = token; }

    public String getSignature() { return this.signature; }
    public void setSignature(String signature) { this.signature = signature; }
}
