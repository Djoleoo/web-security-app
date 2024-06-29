package BSEP.KT2.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BSEP.KT2.security.jwt.IJwtHandler;
import BSEP.KT2.service.IAccountDeletionService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/accounts")
public class AccountDeletionController extends BaseController {

    @Autowired
    private IAccountDeletionService accountDeletionService; 

    @Autowired
    private IJwtHandler jwtHandler;

    private static final Logger logger = LoggerFactory.getLogger(AccountDeletionController.class);
    
    @DeleteMapping()
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Object> deleteAccount(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        String clientHost = request.getRemoteHost();
        int clientPort = request.getRemotePort();
        logger.info("Entering deleteAccount() - Account deletion request from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);

        try {
            String token = extractJwtToken(request);
            String username = jwtHandler.extractUsername(token);
            logger.info("Attempting to delete account for user: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort);
            accountDeletionService.delete(username);
            logger.info("Exiting deleteAccount() - Successfully deleted the account for user: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.OK).body(generateSuccessResponse(200, "Successfully deleted the user account."));
        } catch (SecurityException ex) {
            logger.warn("deleteAccount() - Account deletion failed (forbidden) for user from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort, ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(generateErrorResponse(403, ex.getMessage()));
        } catch (NoSuchElementException ex) {
            logger.warn("deleteAccount() - Account deletion failed (not found) for user from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }
}
