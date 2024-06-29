package BSEP.KT2.dto;

public class TwoFactorAuthenticationSetupCompletionDto {
    private int code;

    public TwoFactorAuthenticationSetupCompletionDto() {
    }

    public TwoFactorAuthenticationSetupCompletionDto(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
