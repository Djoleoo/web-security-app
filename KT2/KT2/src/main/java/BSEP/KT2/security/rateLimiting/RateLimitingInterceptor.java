package BSEP.KT2.security.rateLimiting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import BSEP.KT2.model.User;
import BSEP.KT2.repository.AdvertisementRepository;
import BSEP.KT2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {
    private final int TOO_MANY_REQUESTS = 429;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;
    
    @Autowired
    private RateLimitingRules rules;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String[] uriParts = request.getRequestURI().split("/");
        String username = uriParts[4];
        Integer adId = Integer.parseInt(uriParts[5]);
        
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        advertisementRepository.findById(adId).orElseThrow(() -> new RuntimeException("Advertisement not found"));
        
        if (!rules.isAllowed(username, adId, user.getClientPackage().toString())) {
            response.setStatus(TOO_MANY_REQUESTS);
            return false;
        }
        
        return true;
    }
}
