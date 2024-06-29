package BSEP.KT2.dto;

public class TwoFactorAuthenticationStatusDto {
    private boolean isEnabled;

    public TwoFactorAuthenticationStatusDto() {
    }

    public TwoFactorAuthenticationStatusDto(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
