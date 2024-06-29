package BSEP.KT2.security.rateLimiting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class RateLimitingRules {
    private final ConcurrentHashMap<String, List<LocalDateTime>> visitsMap = new ConcurrentHashMap<>();

    private static final Map<String, Integer> RATE_LIMITS = Map.of(
        "BASE", 10,
        "STANDARD", 100,
        "GOLD", 10000
    );
    private final int TIME_INTERVAL_IN_MINUTES = 1;
    
    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit";
    private static final String RATE_LIMIT_KEY_TEMPLATE = RATE_LIMIT_KEY_PREFIX + "(Username: {username}, AdID:{adID})";
    
    public boolean isAllowed(String username, Integer adId, String packageName) {
        cleanupExpiredRequests();

        String key = RATE_LIMIT_KEY_TEMPLATE
                .replace("{username}", username)
                .replace("{adID}", Integer.toString(adId));

        Integer limit = RATE_LIMITS.getOrDefault(packageName, RATE_LIMITS.get("default"));

        List<LocalDateTime> visitTimes;

        synchronized (visitsMap) {
            visitTimes = visitsMap.computeIfAbsent(key, k -> new ArrayList<>());
        }

        synchronized (visitTimes) {
            visitTimes.add(LocalDateTime.now());
        }

        return visitTimes.size() <= limit;
    }

    private void cleanupExpiredRequests() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(TIME_INTERVAL_IN_MINUTES);
    
        for (Map.Entry<String, List<LocalDateTime>> entry : visitsMap.entrySet()) {
            String key = entry.getKey();
            List<LocalDateTime> visitTimes = entry.getValue();
            
            visitTimes.removeIf(visitTime -> visitTime.isBefore(cutoffTime));
            
            if (visitTimes.isEmpty()) {
                visitsMap.remove(key);
            }
        }
    }
}
