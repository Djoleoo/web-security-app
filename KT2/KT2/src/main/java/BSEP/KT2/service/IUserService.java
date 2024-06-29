package BSEP.KT2.service;


import java.util.List;
import java.util.NoSuchElementException;


import org.springframework.security.core.userdetails.UserDetailsService;

import BSEP.KT2.dto.ClientEmployeeDto;
import BSEP.KT2.dto.PersonalInfoDto;
import BSEP.KT2.dto.UserCreationDto;
import BSEP.KT2.model.User;

public interface IUserService extends UserDetailsService {
    User findByUsername(String username) throws NoSuchElementException;
    
    public void deleteInactiveUsers();
    public void updateAdminInfo(String username,String firstName, String lastName, String address,String city, String country, String phoneNumber) throws Exception;
    public void updateClientInfo(String username,String newusername,String firstName, String lastName, String address,String city, String country, String phoneNumber) throws Exception;
    public List<ClientEmployeeDto> getClientsAndEmployees();
    
    public void adminCreatesUser(UserCreationDto creationDto);
    public PersonalInfoDto findUserByUsername(String username);
    public void blockUser(String username);
    public void initiatePasswordRecovery(String email);
    public void resetPassword(String token, String newPassword);
}
