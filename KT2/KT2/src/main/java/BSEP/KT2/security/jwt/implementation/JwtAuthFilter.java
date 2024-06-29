package BSEP.KT2.security.jwt.implementation;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import BSEP.KT2.security.jwt.IJwtHandler;
import BSEP.KT2.service.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private IJwtHandler jwtHandler;
    @Autowired
    private IUserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authenticationHeader = request.getHeader("Authorization");
        if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            final String jwt = authenticationHeader.substring(7);
            final String username = jwtHandler.extractUsername(jwt);
            if (username != null && !isUserAuthenticated()) {
                try {
                    var userDetails = userService.loadUserByUsername(username);
                    if (jwtHandler.isJwtValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwt, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (NoSuchElementException e) {
                    logger.error("User with username '" + username + "' could not be found.");
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private Boolean isUserAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }
    
}