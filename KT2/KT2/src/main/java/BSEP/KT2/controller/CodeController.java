package BSEP.KT2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
public class CodeController {

    private final GoogleAuthenticator gAuth;
    private static final Logger logger = LoggerFactory.getLogger(CodeController.class);

    @SneakyThrows
    @GetMapping("/generate/{username}")
    public String generate(@PathVariable String username, HttpServletResponse response) {
        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("my-demo", username, key);

        return otpAuthURL;
    }

    @PostMapping("/validate/key/{username}/{code}")
    public boolean validateKey(@PathVariable String username, @PathVariable int code, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        String clientHost = request.getRemoteHost();
        int clientPort = request.getRemotePort();
        logger.info("Entering validateKey() - Validate OTP for user: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort);

        boolean isValid = gAuth.authorizeUser(username, code);

        logger.info("Exiting validateKey() - OTP validation {} for user: {} from IP: {}, HOST: {}, PORT: {}", isValid ? "successful" : "failed", username, clientIp, clientHost, clientPort);
        return isValid;
    }
}