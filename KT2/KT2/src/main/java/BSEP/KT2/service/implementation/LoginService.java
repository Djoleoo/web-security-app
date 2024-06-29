package BSEP.KT2.service.implementation;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import BSEP.KT2.dto.LoginDto;
import BSEP.KT2.dto.LoginTokensDto;
import BSEP.KT2.dto.LoginTwoFactorAuthenticationDto;
import BSEP.KT2.dto.PasswordlessLoginDto;
import BSEP.KT2.dto.PasswordlessLoginRequestDto;
import BSEP.KT2.dto.PasswordlessLoginTwoFactorAuthenticationDto;
import BSEP.KT2.dto.RefreshTokenDto;
import BSEP.KT2.dto.TwoFactorAuthenticationSetupDto;
import BSEP.KT2.dto.TwoFactorAuthenticationStatusDto;
import BSEP.KT2.model.PasswordlessLoginAttempt;
import BSEP.KT2.model.User;
import BSEP.KT2.model.enums.ClientPackage;
import BSEP.KT2.repository.PasswordlessLoginAttemptRepository;
import BSEP.KT2.repository.UserRepository;
import BSEP.KT2.security.encryption.IEncryptor;
import BSEP.KT2.security.hash.IHasher;
import BSEP.KT2.security.hash.ISaltGenerator;
import BSEP.KT2.security.jwt.IJwtHandler;
import BSEP.KT2.security.password.IPasswordGenerator;
import BSEP.KT2.security.reCaptcha.IReCaptchaVerifier;
import BSEP.KT2.security.singleSignOn.IGoogleSingleSignOnHandler;
import BSEP.KT2.security.twoFactorAuthentication.IGoogleTwoFactorAuthenticator;
import BSEP.KT2.service.ILoginService;
import BSEP.KT2.utility.email.IEmailSender;
import BSEP.KT2.utility.email.templates.PasswordlessLoginEmailTemplate;
import BSEP.KT2.utility.email.templates.TemporaryPasswordEmailTemplate;
import BSEP.KT2.utility.exceptions.TwoFactorAuthenticationException;

@Service
public class LoginService implements ILoginService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordlessLoginAttemptRepository attemptRepository;

    @Autowired
    private IHasher hasher;

    @Autowired
    private ISaltGenerator saltGenerator;

    @Autowired
    private IEncryptor encryptor;

    @Autowired
    private IJwtHandler jwtHandler;

    @Autowired
    private IEmailSender emailSender;

    @Autowired
    private IGoogleTwoFactorAuthenticator twoFactorAuthenticator;

    @Autowired
    private IReCaptchaVerifier captchaVerifier;

    @Autowired
    private IGoogleSingleSignOnHandler singleSignOnHandler;

    @Autowired
    private IPasswordGenerator passwordGenerator;

    @Override
    public LoginTokensDto login(LoginDto credentials) throws TwoFactorAuthenticationException {
        User user = userRepository.findByUsername(credentials.getUsername()).orElse(null);
        if(user == null) {
            throw new NoSuchElementException("User with this email doesn't exist."); 
        }

        if(!user.getIsActivated()) {
            throw new SecurityException("You have to activate your account to sign in. Please visit the link we sent you on your email to activate your account."); 
        }

        String hashedPassword = hasher.hashSalted(credentials.getPassword(), user.getPasswordSalt());
        if(!hashedPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        if(user.getIs2FactorAuthenticationEnabled()) {
            throw new TwoFactorAuthenticationException("Two factor authentication needed.");
        }

        if(!captchaVerifier.verify(credentials.getReCaptchaResponse())) {
            throw new SecurityException("Invalid reCAPTCHA answer.");
        }

        if(user.getIsBlocked()){
            throw new IllegalArgumentException("Your account was blocked by administrator.");
        }

        String accessToken = jwtHandler.generateAccessToken(user);
        String refreshToken = jwtHandler.generateRefreshToken(user);
        LoginTokensDto tokens = new LoginTokensDto(accessToken, refreshToken);

        return tokens;
    }

    @Override
    public LoginTokensDto loginTwoFactorAuthentication(LoginTwoFactorAuthenticationDto credentials) throws TwoFactorAuthenticationException {
        User user = userRepository.findByUsername(credentials.getUsername()).orElse(null);
        if(user == null) {
            throw new NoSuchElementException("User with this email doesn't exist."); 
        }

        if(!user.getIsActivated()) {
            throw new SecurityException("You have to activate your account to sign in. Please visit the link we sent you on your email to activate your account."); 
        }

        String hashedPassword = hasher.hashSalted(credentials.getPassword(), user.getPasswordSalt());
        if(!hashedPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        if(!twoFactorAuthenticator.authenticate(credentials.getUsername(), credentials.getCode())) {
            throw new TwoFactorAuthenticationException("Invalid two factor authentication code.");
        }

        if(!captchaVerifier.verify(credentials.getReCaptchaResponse())) {
            throw new SecurityException("Invalid reCAPTCHA answer.");
        }

        String accessToken = jwtHandler.generateAccessToken(user);
        String refreshToken = jwtHandler.generateRefreshToken(user);
        LoginTokensDto tokens = new LoginTokensDto(accessToken, refreshToken);

        return tokens;
    }

    @Override
    public void requestPasswordlessLogin(PasswordlessLoginRequestDto credentials) {
        User user = userRepository.findByUsername(credentials.getUsername()).orElse(null);
        if(user == null) {
            throw new NoSuchElementException("User with this email doesn't exist."); 
        }

        if(!user.getIsActivated()) {
            throw new SecurityException("You have to activate your account to sign in. Please visit the link we sent you on your email to activate your account."); 
        }

        if(user.getClientPackage().equals(ClientPackage.BASE)) {
            throw new SecurityException("Users with the Base service package do not have access to passwordless sign in. Please consider purchasing the Standard or Gold service packages."); 
        }

        if(!captchaVerifier.verify(credentials.getReCaptchaResponse())) {
            throw new SecurityException("Invalid reCAPTCHA answer.");
        }

        PasswordlessLoginAttempt attempt = new PasswordlessLoginAttempt("");
        attemptRepository.save(attempt);

        LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(10);
        String message =  "" + attempt.getId() + "|" + user.getId() + "|" + expirationDateTime.toString();
        String token = encryptor.encrypt(message);
        String signature = hasher.hashHmac(token);

        attempt.setSignature(signature);
        attemptRepository.save(attempt);

        String loginLinkTemplate = "http://localhost:4200/passwordless-sign-in?token=%s&signature=%s";
        String loginLink = String.format(loginLinkTemplate, token, signature);

        String title = PasswordlessLoginEmailTemplate.generateTitle();
        String emailContent = PasswordlessLoginEmailTemplate.generateText(loginLink);
        emailSender.sendHtml(user.getUsername(), title, emailContent);
    }

    @Override
    public LoginTokensDto loginPasswordless(PasswordlessLoginDto credentials) throws TwoFactorAuthenticationException {
        credentials.setToken(credentials.getToken().replace(' ', '+'));
        String message = encryptor.decrypt(credentials.getToken());
        String[] messageParts = message.split("\\|");
        int attemptId = Integer.parseInt(messageParts[0]);
        int userId = Integer.parseInt(messageParts[1]);
        LocalDateTime expiration = LocalDateTime.parse(messageParts[2]);

        PasswordlessLoginAttempt attempt = attemptRepository.findById(attemptId).orElse(null);
        if(attempt == null) {
            throw new SecurityException("Invalid passwordless sign in link."); 
        }

        String trueSignature = hasher.hashHmac(credentials.getToken());
        trueSignature = trueSignature.replace('+', ' ');
        credentials.setSignature(credentials.getSignature().replace('+', ' '));;
        boolean isSignatureValid = credentials.getSignature().equals(trueSignature);
        if(!isSignatureValid) {
            throw new SecurityException("Invalid passwordless sign in link.");
        }
        
        if(expiration.isBefore(LocalDateTime.now())) {
            throw new SecurityException("Passwordless sign in link has expired.");
        }

        User user = userRepository.findById(userId).orElse(null);

        if(user.getIs2FactorAuthenticationEnabled()) {
            throw new TwoFactorAuthenticationException("Two factor authentication needed.");
        }

        if(!captchaVerifier.verify(credentials.getReCaptchaResponse())) {
            throw new SecurityException("Invalid reCAPTCHA answer.");
        }

        attempt.use();
        attemptRepository.save(attempt);
        
        String accessToken = jwtHandler.generateAccessToken(user);
        String refreshToken = jwtHandler.generateRefreshToken(user);
        LoginTokensDto tokens = new LoginTokensDto(accessToken, refreshToken);

        return tokens;
    }

    @Override
    public LoginTokensDto loginPasswordlessTwoFactorAuthentication(PasswordlessLoginTwoFactorAuthenticationDto credentials) throws TwoFactorAuthenticationException {
        credentials.setToken(credentials.getToken().replace(' ', '+'));
        String message = encryptor.decrypt(credentials.getToken());
        String[] messageParts = message.split("\\|");
        int attemptId = Integer.parseInt(messageParts[0]);
        int userId = Integer.parseInt(messageParts[1]);
        LocalDateTime expiration = LocalDateTime.parse(messageParts[2]);

        PasswordlessLoginAttempt attempt = attemptRepository.findById(attemptId).orElse(null);
        if(attempt == null) {
            throw new SecurityException("Invalid passwordless sign in link."); 
        }

        String trueSignature = hasher.hashHmac(credentials.getToken());
        trueSignature = trueSignature.replace('+', ' ');
        credentials.setSignature(credentials.getSignature().replace('+', ' '));;
        boolean isSignatureValid = credentials.getSignature().equals(trueSignature);
        if(!isSignatureValid) {
            throw new SecurityException("Invalid passwordless sign in link.");
        }
        
        if(expiration.isBefore(LocalDateTime.now())) {
            throw new SecurityException("Passwordless sign in link has expired.");
        }

        User user = userRepository.findById(userId).orElse(null);

        if(!twoFactorAuthenticator.authenticate(user.getUsername(), credentials.getCode())) {
            throw new TwoFactorAuthenticationException("Invalid two factor authentication code.");
        }

        if(!captchaVerifier.verify(credentials.getReCaptchaResponse())) {
            throw new SecurityException("Invalid reCAPTCHA answer.");
        }

        attempt.use();
        attemptRepository.save(attempt);
        
        String accessToken = jwtHandler.generateAccessToken(user);
        String refreshToken = jwtHandler.generateRefreshToken(user);
        LoginTokensDto tokens = new LoginTokensDto(accessToken, refreshToken);

        return tokens;
    }

    public User findByUsername(String username) throws NoSuchElementException {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public LoginTokensDto refreshAccessToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        
        // Validate refresh token
        // Here you should validate the refresh token to ensure it's valid
        
        // Extract user information from the refresh token
        String username = jwtHandler.extractUsername(refreshToken);
        User user = findByUsername(username);
        
        // Generate new access token
        String accessToken = jwtHandler.generateAccessToken(user);
        
        // Prepare response with new access token
        LoginTokensDto tokens = new LoginTokensDto(accessToken, refreshToken);
        
        return tokens;
    }

    public Boolean canRefreshToken(String accessToken,String username){
        String usernameFromToken=jwtHandler.extractUsername(accessToken);
        UserDetails userDetails=loadUserByUsername(username);
        return jwtHandler.canRefreshToken(usernameFromToken, userDetails);
    }

    public UserDetails loadUserByUsername(String username) {
        return findByUsername(username);
    }

    @Override
    public void changePassword(String username, String newPassword){
        User user = findByUsername(username);

        user.setPasswordSalt(saltGenerator.generate());
        String hashedPassword = hasher.hashSalted(newPassword, user.getPasswordSalt());
        user.setPassword(hashedPassword);

        user.setIsFirstLogin(false);

        userRepository.save(user);
    }

    public TwoFactorAuthenticationStatusDto is2FactorAuthenticationEnabled(String username) {
        User user = findByUsername(username);

        TwoFactorAuthenticationStatusDto status = new TwoFactorAuthenticationStatusDto(user.getIs2FactorAuthenticationEnabled());

        return status;
    }

    public TwoFactorAuthenticationSetupDto setup2FactorAuthentication(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        if(user.getIs2FactorAuthenticationEnabled()) {
            throw new IllegalArgumentException("Two factor authentication is already enabled.");
        }

        final GoogleAuthenticatorKey key = twoFactorAuthenticator.createCredentials(username);
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("MarkeTeam", username, key);

        String key2FA = encryptor.encrypt(key.getKey());
        user.setKey2FactorAuthentication(key2FA);
        userRepository.save(user);

        TwoFactorAuthenticationSetupDto setup = new TwoFactorAuthenticationSetupDto(otpAuthURL);
        return setup;
    }

    @Override
    public void enable2FactorAuthentication(String username, int code) {
        if(twoFactorAuthenticator.authenticate(username, code)) {
            User user = userRepository.findByUsername(username).orElseThrow();

            user.enable2FactorAuthentication();

            userRepository.save(user);
            return;
        }
        
        throw new SecurityException("Invalid two factor authentication code."); 
    }

    @Override
    public void disable2FactorAuthentication(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        user.disable2FactorAuthentication();

        userRepository.save(user);
    }

    @Override
    public LoginTokensDto loginGoogle(String token) {
        Payload userInfo = singleSignOnHandler.verify(token);

        User user = userRepository.findByUsername((String) userInfo.get("email")).orElse(null);
        if(user != null) {
            String accessToken = jwtHandler.generateAccessToken(user);
            String refreshToken = jwtHandler.generateRefreshToken(user);
            LoginTokensDto tokens = new LoginTokensDto(accessToken, refreshToken);

            return tokens;
        }

        user = new User(userInfo);
        String password = passwordGenerator.generateRandomPassword();
        String salt = saltGenerator.generate();
        user.setPassword(hasher.hashSalted(password, salt));
        user.setPasswordSalt(salt);
        userRepository.save(user);

        String title = TemporaryPasswordEmailTemplate.generateTitle();
        String emailContent = TemporaryPasswordEmailTemplate.generateText(password);
        emailSender.sendHtml(user.getUsername(), title, emailContent);

        String accessToken = jwtHandler.generateAccessToken(user);
        String refreshToken = jwtHandler.generateRefreshToken(user);
        LoginTokensDto tokens = new LoginTokensDto(accessToken, refreshToken);

        return tokens;
    }
}
