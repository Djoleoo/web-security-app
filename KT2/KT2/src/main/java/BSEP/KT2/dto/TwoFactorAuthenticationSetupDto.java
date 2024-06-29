package BSEP.KT2.dto;

public class TwoFactorAuthenticationSetupDto {
    private String qrCode;

    public TwoFactorAuthenticationSetupDto() {
    }

    public TwoFactorAuthenticationSetupDto(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
