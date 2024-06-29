package BSEP.KT2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import BSEP.KT2.model.User;
import BSEP.KT2.model.enums.Role;
import BSEP.KT2.dto.ClientEmployeeDto;
import BSEP.KT2.dto.PersonalInfoDto;;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    
    @Query("SELECT new BSEP.KT2.dto.ClientEmployeeDto(u.username,u.firstName,u.lastName,u.role) FROM User u WHERE u.role = :clientRole OR u.role = :employeeRole")
    List<ClientEmployeeDto> findClientsAndEmployees(@Param("clientRole") Role clientRole, @Param("employeeRole") Role employeeRole);

    @Query("SELECT new BSEP.KT2.dto.PersonalInfoDto(u.username, u.firstName, u.lastName, u.address, u.city, u.country, u.phoneNumber, u.companyName, u.taxIdentificationNumber) FROM User u WHERE u.username = :username")
    PersonalInfoDto findUser(@Param("username") String username);
    
}
