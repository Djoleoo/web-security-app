package BSEP.KT2.service;

import BSEP.KT2.dto.LoginDto;
import BSEP.KT2.dto.LoginTokensDto;
import BSEP.KT2.dto.LoginTwoFactorAuthenticationDto;
import BSEP.KT2.dto.PasswordlessLoginDto;
import BSEP.KT2.dto.PasswordlessLoginRequestDto;
import BSEP.KT2.dto.PasswordlessLoginTwoFactorAuthenticationDto;
import BSEP.KT2.dto.RefreshTokenDto;
import BSEP.KT2.dto.TwoFactorAuthenticationSetupDto;
import BSEP.KT2.dto.TwoFactorAuthenticationStatusDto;
import BSEP.KT2.utility.exceptions.TwoFactorAuthenticationException;

public interface ILoginService {
    public LoginTokensDto login(LoginDto credentials) throws TwoFactorAuthenticationException;
    public LoginTokensDto loginTwoFactorAuthentication(LoginTwoFactorAuthenticationDto credentials) throws TwoFactorAuthenticationException;
    public void requestPasswordlessLogin(PasswordlessLoginRequestDto credentials);
    public LoginTokensDto loginPasswordless(PasswordlessLoginDto credentials) throws TwoFactorAuthenticationException ;
    public LoginTokensDto loginPasswordlessTwoFactorAuthentication(PasswordlessLoginTwoFactorAuthenticationDto credentials) throws TwoFactorAuthenticationException;
    public LoginTokensDto refreshAccessToken(RefreshTokenDto refreshTokenDto);
    public Boolean canRefreshToken(String accessToken,String username);
    public void changePassword(String username, String newPassword);
    public TwoFactorAuthenticationStatusDto is2FactorAuthenticationEnabled(String username);
    public TwoFactorAuthenticationSetupDto setup2FactorAuthentication(String username);
    public void enable2FactorAuthentication(String username, int code);
    public void disable2FactorAuthentication(String username);
    public LoginTokensDto loginGoogle(String token);
}
