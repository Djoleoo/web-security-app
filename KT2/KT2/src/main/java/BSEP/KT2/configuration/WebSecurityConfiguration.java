package BSEP.KT2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import BSEP.KT2.security.jwt.implementation.JwtAuthFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
        "/api/auth/**",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui/**",
        "/webjars/**",
        "/*.html",
        "favicon.ico",
        "/*/*.html",
        "/*/*.css",
        "/*/*.js",
        "/ws/**", "/ws",
        "/actuator","/actuator/*",
        "/swagger-ui.html",
        "/api/login/refresh-token",
        "/api/registration/request",
        "/api/registration/activate-user/**",
        "/api/login",
        "/api/login/google",
        "/api/login/two-factor-authentication",
        "/api/login/request-passwordless",
        "/api/login/passwordless",
        "/api/login/passwordless/two-factor-authentication",
        "/api/login/recover-password",
        "/api/login/reset-password",
        "/api/images/logo",
        "/api/login/users/**",
         "/api/advertisement/getClientsAdvertisements/{username}",
         "/api/admin/{username}",
         "/api/login/changePassword",
         "/api/advertisement/visit/{username}/{adId}",
    };
    
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(req ->
                req.requestMatchers(WHITE_LIST_URL)
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();
    }

}

