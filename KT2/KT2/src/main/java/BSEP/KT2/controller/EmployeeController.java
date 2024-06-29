package BSEP.KT2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import BSEP.KT2.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestParam String username,
                                             @RequestParam String firstName,
                                             @RequestParam String lastName,
                                             @RequestParam String address,
                                             @RequestParam String city,
                                             @RequestParam String country,
                                             @RequestParam String phoneNumber,
                                             HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        logger.info("Entering updateUser() - User information update for: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort);

        try {
            userService.updateAdminInfo(username, firstName, lastName, address, city, country, phoneNumber);
            logger.info("Exiting updateUser() - Successfully updated user information for: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort);
            return ResponseEntity.ok("User information updated successfully");
        } catch (Exception e) {
            logger.error("updateUser() - Failed to update user information for: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort, e);
            return ResponseEntity.status(500).body("Failed to update user information: " + e.getMessage());
        }
    }
}
