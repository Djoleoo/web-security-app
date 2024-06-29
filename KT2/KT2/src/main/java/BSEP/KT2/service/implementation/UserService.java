package BSEP.KT2.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import BSEP.KT2.dto.ClientEmployeeDto;
import BSEP.KT2.dto.PersonalInfoDto;
import BSEP.KT2.model.PasswordResetToken;
import BSEP.KT2.model.User;
import BSEP.KT2.dto.UserCreationDto;
import BSEP.KT2.model.enums.ClientPackage;
import BSEP.KT2.model.enums.Role;
import BSEP.KT2.repository.PasswordResetTokenRepository;
import BSEP.KT2.repository.UserRepository;
import BSEP.KT2.security.hash.IHasher;
import BSEP.KT2.security.hash.ISaltGenerator;
import BSEP.KT2.service.IUserService;
import BSEP.KT2.utility.email.IEmailSender;
import BSEP.KT2.utility.email.templates.AccountRecoveryEmailTemplate;
import jakarta.transaction.Transactional;

@Service
public class UserService implements IUserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IHasher hasher;

    @Autowired
    private ISaltGenerator saltGenerator;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private IEmailSender emailSender;

    @Override
    public User findByUsername(String username) throws NoSuchElementException {
        return userRepository.findByUsername(username).orElseThrow();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return findByUsername(username);
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void deleteInactiveUsers() {
        List<User> users = userRepository.findAll();
        List<User> inactiveUsers = users.stream()
            .filter(u -> u.isInactive()).collect(Collectors.toList());

        for (User user : inactiveUsers) {
            userRepository.delete(user);
        }
    }

    @Override
    public void updateAdminInfo(String username, String firstName, String lastName, String address,String city, String country, String phoneNumber) throws Exception{
        User user = findByUsername(username);
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.EMPLOYEE) {
            // Find the user to be updated
            
            
                // Update user information
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setAddress(address);
                user.setCity(city);
                user.setCountry(country);
                user.setPhoneNumber(phoneNumber);
                // Save the updated user
                userRepository.save(user);
            
        } else {
            throw new Exception("Only admin users can update admin information");
        }
    }

    @Override
    public void updateClientInfo(String username,String newusername, String firstName, String lastName, String address,String city, String country, String phoneNumber) throws Exception{
        User user = findByUsername(username);
        if (user.getRole() == Role.CLIENT) {
            // Find the user to be updated
            
            
                // Update user information
                user.setUsername(newusername);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setAddress(address);
                user.setCity(city);
                user.setCountry(country);
                user.setPhoneNumber(phoneNumber);
                // Save the updated user
                userRepository.save(user);
            
        } else {
            throw new Exception("Only client users can update client information");
        }
    }

    @Override
    public List<ClientEmployeeDto> getClientsAndEmployees() {
        return userRepository.findClientsAndEmployees(Role.CLIENT,Role.EMPLOYEE);
    }
    
    public void adminCreatesUser(UserCreationDto creationDto) {
        User user = new User();
        
        user.setPasswordSalt(saltGenerator.generate());
        String hashedPassword = hasher.hashSalted(creationDto.getPassword(), user.getPasswordSalt());
        user.setPassword(hashedPassword);

        user.setUsername(creationDto.getEmail());
        user.setFirstName(creationDto.getFirstName());
        user.setLastName(creationDto.getLastName());
        user.setCountry("Country");
        user.setCity("City");
        user.setPhoneNumber("+111111111");
        user.setAddress("Adress");
        user.setClientPackage(ClientPackage.STANDARD);
        



        switch (creationDto.getRole().toUpperCase()) {
            case "ADMINISTRATOR":
                user.setRole(Role.ADMIN);
                break;
            case "EMPLOYEE":
                user.setRole(Role.EMPLOYEE);
                break;
            default:
                user.setRole(Role.EMPLOYEE); 
                break;
        }

        user.setIsActivated(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsFirstLogin(true);
        
        userRepository.save(user);
    }

    public PersonalInfoDto findUserByUsername(String username) {
        return userRepository.findUser(username);
    }


    public void blockUser(String username){
        User user = findByUsername(username);
        user.block();
        userRepository.save(user);
    }

    public void initiatePasswordRecovery(String email) {
        User user = findByUsername(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, user);
            passwordResetTokenRepository.save(resetToken);
    
            String resetUrl = "https://localhost:4200/reset-password?token=" + token;

            String title = AccountRecoveryEmailTemplate.generateTitle();
            String emailContent = AccountRecoveryEmailTemplate.generateText(resetUrl);
            emailSender.sendHtml(email, title, emailContent);
        }
    }

    @Transactional
    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        User user = resetToken.getUser();
        String newSalt = saltGenerator.generate();
        String hashedPassword = hasher.hashSalted(newPassword, newSalt);

        user.setPasswordSalt(newSalt);
        user.setPassword(hashedPassword);

        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }
}
