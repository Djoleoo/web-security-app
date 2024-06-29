package BSEP.KT2.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import BSEP.KT2.dto.ClientRegistrationRequestCreationDto;
import BSEP.KT2.dto.ClientRegistrationRequestRejectionDto;
import BSEP.KT2.dto.ClientRegistrationRequestViewDto;
import BSEP.KT2.dto.UserActivationDto;
import BSEP.KT2.dto.UserCreationDto;
import BSEP.KT2.service.IRegistrationService;
import BSEP.KT2.service.IUserService;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController extends BaseController {
    @Autowired
    private IRegistrationService registrationService;

    @Autowired
    private IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @PostMapping("/request")
    public ResponseEntity<Object> request(@RequestBody ClientRegistrationRequestCreationDto request, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering request() - Registration request for user: {} from IP: {}, HOST: {}, PORT: {}", request.getEmail(), clientIp, clientHost, clientPort);

        try {
            registrationService.requestClientRegistration(request);
            logger.info("Exiting request() - Successfully requested sign up for user: {} from IP: {}, HOST: {}, PORT: {}", request.getEmail(), clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.CREATED).body(generateSuccessResponse(201, "Successfully requested sign up."));
        } catch (IllegalArgumentException ex) {
            logger.warn("request() - Registration request failed (bad request) for user: {} from IP: {}, HOST: {}, PORT: {}", request.getEmail(), clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorResponse(400, ex.getMessage()));
        } catch (SecurityException ex) {
            logger.warn("request() - Registration request failed (forbidden) for user: {} from IP: {}, HOST: {}, PORT: {}", request.getEmail(), clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(generateErrorResponse(403, ex.getMessage()));
        } catch (EntityExistsException ex) {
            logger.warn("request() - Registration request failed (conflict) for user: {} from IP: {}, HOST: {}, PORT: {}", request.getEmail(), clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(generateErrorResponse(409, ex.getMessage()));
        }
    }

    @PutMapping("/accept/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> accept(@PathVariable int requestId, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering accept() - Accepting registration request: {} from IP: {}, HOST: {}, PORT: {}", requestId, clientIp, clientHost, clientPort);

        try {
            registrationService.acceptClientRegistration(requestId);
            logger.info("Exiting accept() - Successfully accepted registration request: {} from IP: {}, HOST: {}, PORT: {}", requestId, clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.OK).body(generateSuccessResponse(200, "Successfully accepted registration request."));
        } catch (IllegalArgumentException ex) {
            logger.warn("accept() - Accepting registration request failed (bad request) for request: {} from IP: {}, HOST: {}, PORT: {}", requestId, clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorResponse(400, ex.getMessage()));
        } catch (NoSuchElementException ex) {
            logger.warn("accept() - Accepting registration request failed (not found) for request: {} from IP: {}, HOST: {}, PORT: {}", requestId, clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }

    @PutMapping("/reject/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> reject(@PathVariable int requestId, @RequestBody ClientRegistrationRequestRejectionDto rejection, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering reject() - Rejecting registration request: {} from IP: {}, HOST: {}, PORT: {}", requestId, clientIp, clientHost, clientPort);

        try {
            registrationService.rejectClientRegistration(requestId, rejection);
            logger.info("Exiting reject() - Successfully rejected registration request: {} from IP: {}, HOST: {}, PORT: {}", requestId, clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.OK).body(generateSuccessResponse(200, "Successfully rejected registration request."));
        } catch (IllegalArgumentException ex) {
            logger.warn("reject() - Rejecting registration request failed (bad request) for request: {} from IP: {}, HOST: {}, PORT: {}", requestId, clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorResponse(400, ex.getMessage()));
        } catch (NoSuchElementException ex) {
            logger.warn("reject() - Rejecting registration request failed (not found) for request: {} from IP: {}, HOST: {}, PORT: {}", requestId, clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        }
    }

    @PutMapping("/activate-user")
    public ResponseEntity<Object> activateUser(@RequestBody UserActivationDto activation, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering activateUser() - Activating user account  from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);

        try {
            registrationService.activateUser(activation);
            logger.info("Exiting activateUser() - Successfully activated user account  from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.OK).body(generateSuccessResponse(200, "Successfully activated user account."));
        } catch (SecurityException ex) {
            logger.warn("activateUser() - Activating user account failed (forbidden)  from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(generateErrorResponse(403, ex.getMessage()));
        } catch (NoSuchElementException ex) {
            logger.warn("activateUser() - Activating user account failed (not found)  from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorResponse(404, ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            logger.warn("activateUser() - Activating user account failed (conflict)  from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(generateErrorResponse(409, ex.getMessage()));
        } catch (Exception e) {
            logger.warn("activateUser() - Activating user account failed (invalid activation link)  from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(generateErrorResponse(403, "Invalid activation link."));
        }
    }

    @GetMapping("/client-registration-requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getClientRegistrationRequests(HttpServletRequest httpRequest) {
        
        List<ClientRegistrationRequestViewDto> requests = registrationService.getAllClientRegistrationRequests();
        return ResponseEntity.status(HttpStatus.OK).body(requests);
    }

    @PostMapping("/adminCreatesUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createUser(@RequestBody UserCreationDto creationDto, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering createUser() - Admin creating user from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);

        try {
            userService.adminCreatesUser(creationDto);
            logger.info("Exiting createUser() - Successfully created user from IP: {}, HOST: {}, PORT: {}", clientIp, clientHost, clientPort);
            return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("createUser() - Failed to create user from IP: {}, HOST: {}, PORT: {}",clientIp, clientHost, clientPort, e);
            return new ResponseEntity<>("Failed to create user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
