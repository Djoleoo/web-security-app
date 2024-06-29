package BSEP.KT2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import BSEP.KT2.dto.ClientEmployeeDto;
import BSEP.KT2.dto.PersonalInfoDto;
//import BSEP.KT2.model.User;
import BSEP.KT2.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    // Autowired UserService
    @Autowired
    private IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    // Endpoint to handle user information update
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
        logger.info("Entering updateUser() - Admin user information update for: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort);

        try {
            userService.updateAdminInfo(username, firstName, lastName, address, city, country, phoneNumber);
            logger.info("Exiting updateUser() - Successfully updated admin user information for: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort);
            return ResponseEntity.ok("User information updated successfully");
        } catch (Exception e) {
            logger.error("updateUser() - Failed to update admin user information for: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort, e);
            return ResponseEntity.status(500).body("Failed to update user information: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/clientsAndEmployees")
    public ResponseEntity<List<ClientEmployeeDto>> getClientsAndEmployees() {
        List<ClientEmployeeDto> clientsAndEmployees = userService.getClientsAndEmployees();
        return ResponseEntity.ok(clientsAndEmployees);
    }

    @GetMapping("/{username}")
    public ResponseEntity<PersonalInfoDto> getUserByUsername(@PathVariable String username) {
        PersonalInfoDto userInfo = userService.findUserByUsername(username);
        if (userInfo != null) {
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/block")
    public ResponseEntity<Void> blockUser(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String clientHost = httpRequest.getRemoteHost();
        int clientPort = httpRequest.getRemotePort();
        String username = request.get("username");
        logger.info("Entering blockUser() - Block user request for: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort);

        try {
            userService.blockUser(username);
            logger.info("Exiting blockUser() - Successfully blocked user: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("blockUser() - Failed to block user: {} from IP: {}, HOST: {}, PORT: {}", username, clientIp, clientHost, clientPort, e);
            return ResponseEntity.status(500).build();
        }
    }

    
    

    
}
