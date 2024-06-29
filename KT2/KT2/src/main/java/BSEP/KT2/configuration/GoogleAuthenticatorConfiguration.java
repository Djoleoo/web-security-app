package BSEP.KT2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import BSEP.KT2.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GoogleAuthenticatorConfiguration {

    private final CredentialRepository credentialRepository;

    @Bean
    public GoogleAuthenticator gAuth() {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        googleAuthenticator.setCredentialRepository(credentialRepository);
        return googleAuthenticator;
    }
}
