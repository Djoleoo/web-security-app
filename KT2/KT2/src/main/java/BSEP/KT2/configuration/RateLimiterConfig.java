package BSEP.KT2.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import BSEP.KT2.security.rateLimiting.RateLimitingInterceptor;

@Configuration
public class RateLimiterConfig implements WebMvcConfigurer {
    @Autowired
    private RateLimitingInterceptor rateLimitingInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor)
            .addPathPatterns("/api/advertisement/visit/{username}/{adId}");;
    }
}
