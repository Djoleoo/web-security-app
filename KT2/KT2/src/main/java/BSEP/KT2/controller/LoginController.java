package BSEP.KT2.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import BSEP.KT2.dto.FirstLoginDto;
import BSEP.KT2.dto.LoginDto;
import BSEP.KT2.dto.LoginTokensDto;
import BSEP.KT2.dto.LoginTwoFactorAuthenticationDto;
import BSEP.KT2.dto.PasswordlessLoginDto;
import BSEP.KT2.dto.PasswordlessLoginRequestDto;
import BSEP.KT2.dto.PasswordlessLoginTwoFactorAuthenticationDto;
import BSEP.KT2.dto.RecoverPassswordRequestDto;
import BSEP.KT2.dto.RefreshTokenDto;
import BSEP.KT2.dto.ResetPasswordRequestDto;
import BSEP.KT2.dto.SingleSignOnDto;
import BSEP.KT2.dto.TwoFactorAuthenticationSetupCompletionDto;
import BSEP.KT2.dto.TwoFactorAuthenticationSetupDto;
import BSEP.KT2.dto.TwoFactorAuthenticationStatusDto;
import BSEP.KT2.model.User;
import BSEP.KT2.security.jwt.IJwtHandler;
import lombok.RequiredArgsConstructor;
import BSEP.KT2.service.ILoginService;
import BSEP.KT2.service.IUserService;
import BSEP.KT2.utility.exceptions.TwoFactorAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController extends BaseController {
    @Autowired
    private ILoginService loginService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IJwtHandler jwtHandler;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping()
    public ResponseEntity<Object> login(@RequestBody LoginDto credentials, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        String clientHost = request.getRemoteHost();
        int clientPort = request.getRemotePort();
        logger.info("Entering login() - Login attempt for user: {} from IP: {}, HOST: {}, PORT: {}", credentials.getUsername(), clientIp, clientHost, clientPort);

        try {
            LoginTokensDto tokens = loginService.login(credentials);
            logger.info("Exiting login() - User {} successfully logged in from IP: {}, HOST: {}, PORT: {}", credentials.getUsername(), clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        } catch (IllegalArgumentException ex) {
            logger.warn("login() - User {} failed to log in from IP: {}, HOST: {}, PORT: {} (incorrect password)", credentials.getUsername(), clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorResponse(400, ex.getMessage()));
        } catch (TwoFactorAuthenticationException ex) {
            logger.warn("login() - User {} failed to log in from IP: {}, HOST: {}, PORT: {} (two-factor authentication)", credentials.getUsername(), clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(generateErrorResponse(401, ex.getMessage()));
        } catch (SecurityException ex) {
            logger.warn("login() - User {} failed to log in from IP: {}, HOST: {}, PORT: {} (not activated account)", credentials.getUsername(), clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(generateErrorResponse(403, ex.getMessage()));
        } catch (NoSuchElementException ex) {
            logger.warn("login() - User {} failed to log in from IP: {}, HOST: {}, PORT: {} (username doesn't exist)", credentials.getUsername(), clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }

    @PostMapping("/two-factor-authentication")
    public ResponseEntity<Object> loginTwoFactorAuthentication(@RequestBody LoginTwoFactorAuthenticationDto credentials) {
        logger.info("Entering loginTwoFactorAuthentication() - Two-factor authentication attempt for user: {}", credentials.getUsername());
        try {
            LoginTokensDto tokens = loginService.loginTwoFactorAuthentication(credentials);
            logger.info("Exiting loginTwoFactorAuthentication() - User {} successfully completed two-factor authentication", credentials.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        } catch (IllegalArgumentException ex) {
            logger.warn("loginTwoFactorAuthentication() - User {} failed two-factor authentication (incorrect code)", credentials.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorResponse(400, ex.getMessage()));
        } catch (TwoFactorAuthenticationException ex) {
            logger.warn("loginTwoFactorAuthentication() - User {} failed two-factor authentication (two-factor authentication exception)", credentials.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(generateErrorResponse(401, ex.getMessage()));
        } catch (SecurityException ex) {
            logger.warn("loginTwoFactorAuthentication() - User {} failed two-factor authentication (not activated account)", credentials.getUsername());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(generateErrorResponse(403, ex.getMessage()));
        } catch (NoSuchElementException ex) {
            logger.warn("loginTwoFactorAuthentication() - User {} failed two-factor authentication (username doesn't exist)", credentials.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }

    @PostMapping("/request-passwordless")
    public ResponseEntity<Object> requestPasswordlessLogin(@RequestBody PasswordlessLoginRequestDto credentials) {
        logger.info("Entering requestPasswordlessLogin() - Passwordless login request for user: {}", credentials.getUsername());
        try {
            loginService.requestPasswordlessLogin(credentials);
            logger.info("Exiting requestPasswordlessLogin() - Passwordless login request successful for user: {}", credentials.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(generateSuccessResponse(201, "Successfully requested passwordless sign in. To continue please visit the link we sent to your email."));
        } catch (SecurityException ex) {
            logger.warn("requestPasswordlessLogin() - Passwordless login request failed (not activated account) for user: {}", credentials.getUsername());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(generateErrorResponse(403, ex.getMessage()));
        } catch (NoSuchElementException ex) {
            logger.warn("requestPasswordlessLogin() - Passwordless login request failed (username doesn't exist) for user: {}", credentials.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }

    @PostMapping("/passwordless")
    public ResponseEntity<Object> loginPasswordless(@RequestBody PasswordlessLoginDto credentials) {
        logger.info("Entering loginPasswordless() - Passwordless login attempt ");
        try {
            LoginTokensDto tokens = loginService.loginPasswordless(credentials);
            logger.info("Exiting loginPasswordless() - successfully logged in passwordlessly");
            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        } catch (TwoFactorAuthenticationException ex) {
            logger.warn("loginPasswordless() -  failed passwordless login (two-factor authentication)");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(generateErrorResponse(401, ex.getMessage()));
        } catch (SecurityException ex) {
            logger.warn("loginPasswordless() -  failed passwordless login (not activated account)");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(generateErrorResponse(403, ex.getMessage()));
        } catch (Exception e) {
            logger.warn("loginPasswordless() -  failed passwordless login (invalid link)" );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(generateErrorResponse(403, "Invalid passwordless sign in link."));
        }
    }

    @PostMapping("/passwordless/two-factor-authentication")
    public ResponseEntity<Object> loginPasswordlessTwoFactorAuthentication(@RequestBody PasswordlessLoginTwoFactorAuthenticationDto credentials) {
        logger.info("Entering loginPasswordlessTwoFactorAuthentication() - Passwordless two-factor authentication attempt ");
        try {
            LoginTokensDto tokens = loginService.loginPasswordlessTwoFactorAuthentication(credentials);
            logger.info("Exiting loginPasswordlessTwoFactorAuthentication() - successfully completed passwordless two-factor authentication" );
            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        } catch (TwoFactorAuthenticationException ex) {
            logger.warn("loginPasswordlessTwoFactorAuthentication() -  failed passwordless two-factor authentication (two-factor authentication)");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(generateErrorResponse(401, ex.getMessage()));
        } catch (SecurityException ex) {
            logger.warn("loginPasswordlessTwoFactorAuthentication() -  failed passwordless two-factor authentication (not activated account)");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(generateErrorResponse(403, ex.getMessage()));
        } catch (Exception e) {
            logger.warn("loginPasswordlessTwoFactorAuthentication() - failed passwordless two-factor authentication (invalid link)");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(generateErrorResponse(403, "Invalid passwordless sign in link."));
        }
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<FirstLoginDto> getUserByUsername(@PathVariable String username) {
       
        try {
            User user = userService.findByUsername(username);
            FirstLoginDto firstLoginDto = new FirstLoginDto(user.getUsername(), user.getIsFirstLogin());
        
            return ResponseEntity.ok(firstLoginDto);
        } catch (NoSuchElementException e) {
           
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginTokensDto> refreshAccessToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        logger.info("Entering refreshAccessToken() - Refresh access token request");
        try {
            LoginTokensDto tokens = loginService.refreshAccessToken(refreshTokenDto);
            logger.info("Exiting refreshAccessToken() - Access token refreshed successfully");
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            logger.warn("refreshAccessToken() - Failed to refresh access token");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam String username, @RequestParam String newPassword) {
        logger.info("Entering changePassword() - Change password request for user: {}", username);
        try {
            loginService.changePassword(username, newPassword);
            logger.info("Exiting changePassword() - Password changed successfully for user: {}", username);
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("changePassword() - Failed to change password for user: {}", username, e);
            return new ResponseEntity<>("Failed to change password: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/check-2-factor-authentication")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<Object> check2FactorAuthentication(HttpServletRequest request) {
        try {
            String token = extractJwtToken(request);
            String username = jwtHandler.extractUsername(token);
            TwoFactorAuthenticationStatusDto status = loginService.is2FactorAuthenticationEnabled(username);
            return ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }

    @PostMapping("/setup-2-factor-authentication")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<Object> setup2FactorAuthentication(HttpServletRequest request) {
        logger.info("Entering setup2FactorAuthentication() - Setup two-factor authentication request");
        try {
            String token = extractJwtToken(request);
            String username = jwtHandler.extractUsername(token);
            TwoFactorAuthenticationSetupDto setup = loginService.setup2FactorAuthentication(username);
            logger.info("Exiting setup2FactorAuthentication() - Two-factor authentication setup initialized for user: {}", username);
            return ResponseEntity.status(HttpStatus.OK).body(setup);
        } catch (IllegalArgumentException ex) {
            logger.warn("setup2FactorAuthentication() - Two-factor authentication setup failed (already enabled)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorResponse(400, "Two factor authentication is already enabled."));
        } catch (NoSuchElementException ex) {
            logger.warn("setup2FactorAuthentication() - Two-factor authentication setup failed (username not found)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }

    @PutMapping("/enable-2-factor-authentication")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<String> enable2FactorAuthentication(@RequestBody TwoFactorAuthenticationSetupCompletionDto setup, HttpServletRequest request) {
        logger.info("Entering enable2FactorAuthentication() - Enable two-factor authentication request");
        try {
            String token = extractJwtToken(request);
            String username = jwtHandler.extractUsername(token);
            loginService.enable2FactorAuthentication(username, setup.getCode());
            logger.info("Exiting enable2FactorAuthentication() - Two-factor authentication enabled for user: {}", username);
            return ResponseEntity.status(HttpStatus.OK).body(generateSuccessResponse(200, "Two factor authentication enabled successfully."));
        } catch (IllegalArgumentException ex) {
            logger.warn("enable2FactorAuthentication() - Enable two-factor authentication failed (already enabled)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorResponse(400, "Two factor authentication is already enabled."));
        } catch (SecurityException ex) {
            logger.warn("enable2FactorAuthentication() - Enable two-factor authentication failed (security exception)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(403, ex.getMessage()));
        } catch (NoSuchElementException ex) {
            logger.warn("enable2FactorAuthentication() - Enable two-factor authentication failed (username not found)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }

    @PutMapping("/disable-2-factor-authentication")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<String> disable2FactorAuthentication(HttpServletRequest request) {
        logger.info("Entering disable2FactorAuthentication() - Disable two-factor authentication request");
        try {
            String token = extractJwtToken(request);
            String username = jwtHandler.extractUsername(token);
            loginService.disable2FactorAuthentication(username);
            logger.info("Exiting disable2FactorAuthentication() - Two-factor authentication disabled for user: {}", username);
            return ResponseEntity.status(HttpStatus.OK).body(generateSuccessResponse(200, "Two factor authentication disabled successfully."));
        } catch (IllegalArgumentException ex) {
            logger.warn("disable2FactorAuthentication() - Disable two-factor authentication failed (already disabled)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorResponse(400, "Two factor authentication is already disabled."));
        } catch (NoSuchElementException ex) {
            logger.warn("disable2FactorAuthentication() - Disable two-factor authentication failed (username not found)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<Object> loginGoogle(@RequestBody SingleSignOnDto token, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering visit() - Visit advertisement request  from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);
        try {
            LoginTokensDto tokens = loginService.loginGoogle(token.getToken());
            logger.info("Exiting visit() - Successfully retrieved advertisement from IP: {}, HOST: {}, PORT: {}",clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        } catch (SecurityException ex) {
            logger.warn("visit() - Advertisement not found from IP: {}, HOST: {}, PORT: {}",clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(generateErrorResponse(403, ex.getMessage()));
        }
    }

    @PostMapping("/recover-password")
    public ResponseEntity<Void> recoverPassword(@RequestBody RecoverPassswordRequestDto request, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering recoverPassword() - Password recovery request for email: {} from IP: {}, HOST: {}, PORT: {}", request.getEmail(), clientIp, clientHost, clientPort);
        
        userService.initiatePasswordRecovery(request.getEmail());
        logger.info("Exiting recoverPassword() - Password recovery initiated for email: {} from IP: {}, HOST: {}, PORT: {}", request.getEmail(), clientIp, clientHost, clientPort);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequestDto request, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering resetPassword() - Password reset request for token: {} from IP: {}, HOST: {}, PORT: {}", request.getToken(), clientIp, clientHost, clientPort);
        
        userService.resetPassword(request.getToken(), request.getNewPassword());
        logger.info("Exiting resetPassword() - Password reset successful for token: {} from IP: {}, HOST: {}, PORT: {}", request.getToken(), clientIp, clientHost, clientPort);
        return ResponseEntity.ok().build();
    }
}
